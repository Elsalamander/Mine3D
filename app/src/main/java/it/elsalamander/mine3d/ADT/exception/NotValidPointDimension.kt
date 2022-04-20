package it.elsalamander.mine3d.ADT.exception

class NotValidPointDimension : InvalidPoint {
    /**
     *
     */
    constructor() : super("Dimensione del punto non valido!") {}

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    constructor(
        message: String?, cause: Throwable?, enableSuppression: Boolean,
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
        private const val serialVersionUID = -4794268980669051723L
    }
}