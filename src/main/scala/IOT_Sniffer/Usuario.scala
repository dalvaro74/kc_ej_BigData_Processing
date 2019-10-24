package IOT_Sniffer

class Usuario(pUserId: String, pNombre: String, pApellido: String, pEdad: Int, pSexo: String) {
    private val _UserId: String = pUserId
    private val _Nombre: String = pNombre
    private val _Apellido: String = pApellido
    private val _Edad: Int = pEdad
    private val _Sexo: String = pSexo

    def UserId: String = this._UserId
    def Nombre: String = this._Nombre
    def Apellido: String = this._Apellido
    def Edad: Int = this._Edad
    def Sexo: String = this._Sexo
}
