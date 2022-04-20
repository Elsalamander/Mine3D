package it.elsalamander.mine3d.ADT.exception

open class InvalidPoint : ADTException {

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace) {
    }

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    /**
     * @param message
     */
    constructor(message: String?) : super(message) {}

    /**
     * @param cause
     */
    constructor(cause: Throwable?) : super(cause) {}

    companion object {
        /**
         *
         */
        private const val serialVersionUID = -6463425001627399629L
    }
}
