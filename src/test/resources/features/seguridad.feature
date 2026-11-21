# language: es
Característica: Seguridad de la sesión
  Como responsable de seguridad de CIBERTEC
  Quiero validar la rotación de tokens, cierre de sesión, bloqueo por intentos y control de roles
  Para proteger la integridad de las cuentas

  Escenario: Rotación del refresh token
    Dado que Ana consulta la API de matricula
    Y inicia sesion con usuario "i202610001" y clave "Cibertec2026"
    Cuando renueva el token usando el refresh token actual
    Entonces el codigo de respuesta es 200
    Y la respuesta cumple el esquema "token.json"
    Cuando intenta renovar el token reutilizando el refresh token original
    Entonces el codigo de respuesta es 401
    Y el codigo de error de seguridad es "REFRESH_REUTILIZADO"
    Y la respuesta cumple el esquema "error.json"
    Cuando consulta su perfil con el access token nuevo
    Entonces el codigo de respuesta es 401
    Y el codigo de error de seguridad es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Cierre de sesión
    Dado que Ana consulta la API de matricula
    Y inicia sesion con usuario "i202610001" y clave "Cibertec2026"
    Cuando cierra su sesion actual
    Entonces el codigo de respuesta es 204
    Cuando consulta su perfil con el access token actual
    Entonces el codigo de respuesta es 401
    Y el codigo de error de seguridad es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Bloqueo de cuenta por intentos fallidos
    Dado que Jorge consulta la API de matricula
    Cuando intenta iniciar sesion con usuario "i202610004" y los siguientes intentos:
      | intento | clave       | status | codigo                 |
      | 1       | ClaveMala01 | 401    | CREDENCIALES_INVALIDAS |
      | 2       | ClaveMala02 | 401    | CREDENCIALES_INVALIDAS |
      | 3       | ClaveMala03 | 401    | CREDENCIALES_INVALIDAS |
      | 4       | ClaveMala04 | 401    | CREDENCIALES_INVALIDAS |
      | 5       | ClaveMala05 | 423    | CUENTA_BLOQUEADA       |
    Y intenta iniciar sesion con usuario "i202610004" y clave "Cibertec2026"
    Entonces el codigo de respuesta es 423
    Y el codigo de error de seguridad es "CUENTA_BLOQUEADA"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Alumno sin permiso de administrador
    Dado que Ana consulta la API de matricula
    Y inicia sesion con usuario "i202610001" y clave "Cibertec2026"
    Cuando intenta listar todos los alumnos
    Entonces el codigo de respuesta es 403
    Y el codigo de error de seguridad es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"