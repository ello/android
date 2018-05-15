package co.ello.android.ello

import java.util.*


interface AuthenticationEndpoint {
    val requiresAnyToken: Boolean
    val supportsAnonymousToken: Boolean
}

typealias RequestAttempt = Triple<AuthenticationEndpoint, Block, Block>

class AuthenticationManager(val requestQueue: Queue) {

    companion object {
        private var waitList: MutableList<RequestAttempt> = mutableListOf()
        private var uuid: UUID = randomUUID()
    }

    private var authState: AuthState
        get() { return AuthToken.state }
        set(value) { AuthToken.state = value }

    // set queue to null in specs, and reauth requests are sent synchronously.
    var queue: DispatchQueue? = DispatchQueue()

    fun attemptRequest(target: AuthenticationEndpoint, retry: (() -> Unit), proceed: ((UUID) -> Unit), cancel: (() -> Unit)) {
        val uuid = AuthenticationManager.uuid

        if (authState.isUndetermined) {
            attemptAuthentication(uuid, RequestAttempt(target, retry, cancel))
        }
        else if (authState.isTransitioning && target.requiresAnyToken) {
            AuthenticationManager.waitList.add(RequestAttempt(target, retry, cancel))
        }
        else {
            if (canMakeRequest(target)) {
                proceed(uuid)
            }
            else {
                cancel()
            }
        }
    }

    fun canMakeRequest(target: AuthenticationEndpoint): Boolean {
        if (!target.requiresAnyToken) {
            return true
        }

        if (authState.isTransitioning) {
            return false
        }

        if (authState.isAuthenticated) {
            return true
        }

        return target.supportsAnonymousToken && authState == AuthState.Anonymous
    }

    fun attemptAuthentication(uuid: UUID, request: RequestAttempt? = null) {
        inBackground(fun () {
            val shouldResendRequest = uuid != AuthenticationManager.uuid
            val retry = request?.let { (_, retry, _) -> retry }
            if (retry != null && shouldResendRequest) {
                retry()
                return
            }

            if (request != null) {
                AuthenticationManager.waitList.add(request)
            }

            when(this.authState) {
                AuthState.Initial -> {
                    val authToken = AuthToken.shared
                    this.advanceAuthState(when(authToken.type) {
                        AuthToken.Type.Password -> AuthState.Authenticated
                        AuthToken.Type.Anonymous -> AuthState.Anonymous
                        AuthToken.Type.None -> AuthState.ShouldTryAnonymousCreds
                    })
                }
                AuthState.Anonymous -> {
                    this.advanceAuthState(AuthState.NoToken)
                }
                AuthState.Authenticated, AuthState.ShouldTryRefreshToken -> {
                    this.authState = AuthState.RefreshTokenSent

                    val refreshToken= AuthToken.shared.refreshToken
                    if (refreshToken != null) {
                        API().reauth(refreshToken)
                            .enqueue(requestQueue)
                            .onSuccess { credentials ->
                                AuthToken.update(credentials)
                                this.advanceAuthState(AuthState.Authenticated)
                            }
                            .onFailure {
                                this.advanceAuthState(AuthState.ShouldTryAnonymousCreds)
                            }
                    }
                    else {
                        this.advanceAuthState(AuthState.ShouldTryAnonymousCreds)
                    }
                }
                AuthState.ShouldTryAnonymousCreds, AuthState.NoToken -> {
                    this.authState = AuthState.AnonymousCredsSent

                    API().anonymousCreds()
                        .enqueue(requestQueue)
                        .onSuccess { credentials ->
                            AuthToken.update(credentials)
                            this.advanceAuthState(AuthState.Anonymous)
                        }.onFailure {
                            this.advanceAuthState(AuthState.NoToken)
                        }
                }
                AuthState.RefreshTokenSent, AuthState.AnonymousCredsSent -> {}
            }
        })
    }

    private fun inBackground(block: Block) {
        queue?.let {
            it.async(block)
        } ?: block()
    }

    private fun advanceAuthState(nextState: AuthState) {
        inBackground {
            this.authState = nextState

            if (nextState == AuthState.NoToken) {
                AuthenticationManager.uuid = randomUUID()
                AuthToken.reset()

                this.flushWaitList()
            }
            else if (nextState == AuthState.Anonymous || nextState.isAuthenticated) {
                AuthenticationManager.uuid = randomUUID()
                this.flushWaitList()
            }
            else {
                this.attemptAuthentication(AuthenticationManager.uuid)
            }
        }
    }

    private fun flushWaitList() {
        val currentWaitList = waitList
        AuthenticationManager.waitList = mutableListOf()

        val runWaitList = {
            for (waitListEntry in currentWaitList) {
                val (target, makeRequest, cancelRequest) = waitListEntry
                if (this.canMakeRequest(target)) {
                    makeRequest()
                }
                else {
                    cancelRequest()
                }
            }
        }

        if (queue == null) {
            runWaitList()
        }
        else {
            DispatchQueue.main.async(runWaitList)
        }
    }
}
