# language: es
Característica: Calificación de matrículas
  Como docente de CIBERTEC
  Quiero registrar las notas finales de los alumnos en mis cursos
  Para actualizar su historial académico

  Esquema del escenario: El docente registra la nota y el historial se actualiza
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con usuario "i202610005" y clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PI5620"
    Dado que Marco consulta la API de matricula
    Y inicia sesion con usuario "D0001" y clave "Cibertec2026"
    Cuando registra la nota <nota> en la matricula obtenida
    Entonces el codigo de respuesta es 200
    Y la condicion de la matricula calificada es "<condicion>"
    Cuando Rosa consulta el historial del alumno "i202610005"
    Entonces el codigo de respuesta es 200
    Y la respuesta cumple el esquema "historial.json"
    Y el historial muestra <creditosAprobados> creditos aprobados y promedio ponderado <promedioPonderado>

    Ejemplos:
      | nota | condicion   | creditosAprobados | promedioPonderado |
      | 11   | DESAPROBADO | 7                 | 13,38             |
      | 12,5 | DESAPROBADO | 7                 | 14,08             |

  Escenario: Un docente que no dicta el curso intenta registrar la nota
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con usuario "i202610005" y clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PI5620"
    Dado que Lucia consulta la API de matricula
    Y inicia sesion con usuario "D0002" y clave "Cibertec2026"
    Cuando intenta registrar la nota 15 en la matricula obtenida
    Entonces el codigo de respuesta es 403
    Y el codigo de error es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: No se puede calificar una matricula que no esta pagada
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con usuario "i202610005" y clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PS5511"
    Dado que Marco consulta la API de matricula
    Y inicia sesion con usuario "D0001" y clave "Cibertec2026"
    Cuando intenta registrar la nota 15 en la matricula obtenida
    Entonces el codigo de respuesta es 409
    Y el codigo de error es "MATRICULA_NO_PAGADA"
    Y la respuesta cumple el esquema "error.json"