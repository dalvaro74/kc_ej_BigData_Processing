package IOT_Sniffer

import java.sql.Timestamp

class Mensaje(pMensajeId: String, pContenidoMensaje: String, pUserId: String, pIoTId: Int) {
    private val _Mensaje_Id: String = pMensajeId
    private val _ContenidoMensaje: String = pContenidoMensaje
    private val _UserId: String = pUserId
    private var _Timestamp: Timestamp = _
    private var _IotId: Int = pIoTId

    def Mensaje_Id: String = this._Mensaje_Id
    def ContenidoMensaje: String = this._ContenidoMensaje
    def UserId: String = this._UserId
    def Timestamp: Timestamp = this._Timestamp
    def IotId: Int = this._IotId

    def Timestamp_=(nuevoValor: Timestamp): Unit = {
        this._Timestamp = nuevoValor
    }
}
