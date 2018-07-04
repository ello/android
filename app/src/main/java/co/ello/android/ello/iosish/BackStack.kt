package co.ello.android.ello


class BackStack(val controller: Controller) {
    enum class StatusCode {
        Success, Failure;
    }

    fun goBack(): StatusCode {
        if (!controller.canGoBack)  return BackStack.StatusCode.Failure

        controller.goBack()
        return BackStack.StatusCode.Success
    }
}
