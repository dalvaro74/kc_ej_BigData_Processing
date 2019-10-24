package IOT_Sniffer

import java.sql.Timestamp

class Mensaje(pMensajeId: String, pContenidoMensaje: String, pUserId: String) {
    private val _Mensaje_Id: String = pMensajeId
    private val _ContenidoMensaje: String = pContenidoMensaje
    private val _UserId: String = pUserId
    private var _Timestamp: Timestamp = _

    def Mensaje_Id: String = this._Mensaje_Id
    def ContenidoMensaje: String = this._ContenidoMensaje
    def UserId: String = this._UserId
    def Timestamp: Timestamp = this._Timestamp

    def Timestamp_=(nuevoValor: Timestamp): Unit = {
        this._Timestamp = nuevoValor
    }
}
