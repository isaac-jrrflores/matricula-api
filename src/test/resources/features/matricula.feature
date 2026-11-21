# language: es
Característica: Matrícula del alumno
  Como alumno de CIBERTEC
  Quiero cotizar, matricularme y pagar mis cursos
  Para gestionar mi carga académica del periodo

  Antecedentes:
    Dado que Ana consulta la API de matricula
    Y inicia sesion con usuario "i202610001" y clave "Cibertec2026"

  Escenario: Cotizar el curso de tu fila
    Cuando cotiza para el alumno "i202610001" el curso "PI5620"
    Entonces el codigo de respuesta es 200
    Y la cotizacion tiene 6 creditos, subtotal 720,0, descuento 20 y total 576,0
    Y la respuesta cumple el esquema "cotizacion.json"

  Escenario: Matricularse y pagar el curso de tu fila
    Cuando se matricula con el codigo "i202610001" en el curso "PI5620"
    Entonces el codigo de respuesta es 201
    Y el monto de la matricula es 576,0
    Y el estado de la matricula es "REGISTRADA"
    Y la respuesta cumple el esquema "matricula.json"
    Cuando paga la matricula registrada con el metodo "TARJETA"
    Entonces el codigo de respuesta es 200
    Y el estado de la matricula es "PAGADA"
    Y la matricula tiene fecha de pago registrada
    Cuando consulta los pagos del alumno
    Entonces el codigo de respuesta es 200
    Y existe el pago de la matricula con metodo "TARJETA" y formato de operacion valido

  Escenario: Regla de negocio que debe fallar por limite de creditos
    Cuando cotiza para el alumno "i202610001" los cursos "PS5511, QA5517, DM5512, TS5514, SA5516, BD5410"
    Entonces el codigo de respuesta es 422
    Y el codigo de error es "LIMITE_CREDITOS"
    Y la respuesta cumple el esquema "error.json"