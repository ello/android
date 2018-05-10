package co.ello.android.ello


abstract class BaseController(a: AppActivity) : Controller(a) {
    var currentUser: User? = null
        set(value) {
            field = value
            didSetCurrentUser()
            for (controller in childControllers) {
                if (controller !is BaseController)  continue
                controller.currentUser = value
            }
        }
    val isLoggedIn: Boolean get() = currentUser != null

    fun didSetCurrentUser() {}
}
