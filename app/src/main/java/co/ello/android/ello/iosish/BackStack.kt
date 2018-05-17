package co.ello.android.ello


class BackStack(val controller: Controller) {
    enum class StatusCode {
        Success, Failure;
    }

    fun goBack(): StatusCode {
        if (!controller.canGoBack)  return Failure

        controller.goBack()
        return Success
    }
}

val Success = BackStack.StatusCode.Success
val Failure = BackStack.StatusCode.Failure
