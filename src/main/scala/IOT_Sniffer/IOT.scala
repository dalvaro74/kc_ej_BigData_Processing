package IOT_Sniffer

class IOT(pId: Int, pEncendido: Boolean, pZona_Id: String) {
    private val _Id: Int = pId
    private val _Encendido: Boolean = pEncendido
    private val _Zona_Id: String = pZona_Id

    def Id: Int = this._Id
    def Encencido: Boolean = this._Encendido
    def Zona_Id: String = this._Zona_Id
}
