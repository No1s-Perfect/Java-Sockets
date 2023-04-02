host cls
PROMPT ======================Jesus==========================
CREATE OR REPLACE PACKAGE types
AS
     TYPE ref_cursor IS REF CURSOR;
END;
/

PROMPT ======================Ciclo==========================
CREATE TABLE ciclo(
id int,
numero int,
fechaInicio VARCHAR(8),
fechaFin VARCHAR(8),
CONSTRAINTS pkciclo PRIMARY KEY (id)
);

create sequence sec_ciclo start with 1;

CREATE OR REPLACE PROCEDURE insertarciclo(numero IN ciclo.numero%TYPE,fechaInicio IN ciclo.fechaInicio%TYPE,
fechaFin IN ciclo.fechaFin%TYPE)
AS
BEGIN
	INSERT INTO ciclo VALUES(sec_ciclo.NEXTVAL,numero,fechaInicio,fechaFin);
END;
/

CREATE OR REPLACE PROCEDURE modificarciclo(idin IN ciclo.id%TYPE,numeroin IN ciclo.numero%TYPE,fechaInicioin IN ciclo.fechaInicio%TYPE,
fechaFinin in ciclo.fechaFin%TYPE)
AS
BEGIN
	UPDATE ciclo SET numero=numeroin,fechaInicio=fechaInicioin,fechaFin=fechaFinin WHERE id=idin;
END;
/

CREATE OR REPLACE FUNCTION buscarciclo(idbuscar IN ciclo.id%TYPE)
RETURN Types.ref_cursor 
AS 
        ciclo_cursor types.ref_cursor; 
BEGIN 
  OPEN ciclo_cursor FOR 
       SELECT id,numero,fechaInicio,fechaFin FROM ciclo WHERE id=idbuscar; 
RETURN ciclo_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarciclos
RETURN Types.ref_cursor 
AS 
        ciclo_cursor types.ref_cursor; 
BEGIN 
  OPEN ciclo_cursor FOR 
       SELECT id,numero,fechaInicio,fechaFin FROM ciclo ; 
RETURN ciclo_cursor; 
END;
/


create or replace procedure eliminarciclo(idin IN ciclo.id%TYPE)
as
begin
    delete from ciclo where id=idin;
end;
/
PROMPT ======================Usuarios==========================
CREATE TABLE usuario(
id int,
rol int,
clave VARCHAR(10),
nombre VARCHAR(30),
CONSTRAINTS pkusuario PRIMARY KEY (id)
);

CREATE OR REPLACE PROCEDURE insertarusuario(id IN usuario.id%TYPE,rol IN usuario.rol%TYPE,
clave IN usuario.clave%TYPE, nombre IN usuario.nombre%TYPE)
AS
BEGIN
	INSERT INTO usuario VALUES(id,rol,clave,nombre);
END;
/

CREATE OR REPLACE PROCEDURE modificarusuario(idn IN usuario.id%TYPE,
rolNuevo IN usuario.rol%TYPE,claveNuevo IN usuario.clave%TYPE,
nombreNuevo in usuario.nombre%TYPE)
AS
BEGIN
	UPDATE usuario 
	SET rol=rolNuevo,clave=claveNuevo,nombre=nombreNuevo WHERE id=idn;
END;
/

CREATE OR REPLACE FUNCTION buscarusuario(idbuscar IN usuario.id%TYPE)
RETURN Types.ref_cursor 
AS 
        usuario_cursor types.ref_cursor; 
BEGIN 
  OPEN usuario_cursor FOR 
       SELECT id,rol,clave,nombre FROM usuario WHERE id=idbuscar; 
RETURN usuario_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarusuario
RETURN Types.ref_cursor 
AS 
        usuario_cursor types.ref_cursor; 
BEGIN 
  OPEN usuario_cursor FOR 
       SELECT id,rol,clave,nombre FROM usuario; 
RETURN usuario_cursor; 
END;
/

create or replace procedure eliminarusuario(idin IN usuario.id%TYPE)
as
begin
    delete from usuario where id=idin;
end;
/

PROMPT ======================Carrera==========================
CREATE TABLE carrera(
codigo VARCHAR(4),
nombre VARCHAR(50),
titulo VARCHAR(30),
CONSTRAINTS pkCarrera PRIMARY KEY (codigo)
);

CREATE OR REPLACE PROCEDURE insertarCarrera(codigo IN carrera.codigo%TYPE,
nombre IN carrera.nombre%TYPE, titulo IN carrera.titulo%TYPE)
AS
BEGIN
	INSERT INTO carrera VALUES(codigo,nombre,titulo);
END;
/

CREATE OR REPLACE PROCEDURE modificarCarrera(codigoIn IN carrera.codigo%TYPE,
nombreNuevo IN carrera.nombre%TYPE,tituloNuevo IN carrera.titulo%TYPE)
AS
BEGIN
	UPDATE carrera 
	SET nombre=nombreNuevo, titulo=tituloNuevo WHERE codigo=codigoIn;
END;
/

CREATE OR REPLACE FUNCTION buscarCarrera(codigoBuscar IN carrera.codigo%TYPE)
RETURN Types.ref_cursor 
AS 
        carrera_cursor types.ref_cursor; 
BEGIN 
  OPEN carrera_cursor FOR 
       SELECT codigo, nombre, titulo FROM carrera WHERE codigo=codigoBuscar; 
RETURN carrera_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarCarreras
RETURN Types.ref_cursor 
AS 
        carrera_cursor types.ref_cursor; 
BEGIN 
  OPEN carrera_cursor FOR 
       SELECT codigo,nombre,titulo FROM carrera; 
RETURN carrera_cursor; 
END;
/

create or replace procedure eliminarCarrera(codigoIn IN carrera.codigo%TYPE)
as
begin
    delete from carrera where codigo=codigoIn;
end;
/
PROMPT ======================Alumnos==========================
CREATE TABLE alumno(
id int,
nombre VARCHAR(100),
telefono VARCHAR(15),
email VARCHAR(100),
fechaNacimiento VARCHAR(8),
carrera VARCHAR(4),
CONSTRAINTS pkAlumno PRIMARY KEY (id),
CONSTRAINTS FKCarrera FOREIGN KEY (carrera) REFERENCES carrera(codigo)
);

CREATE OR REPLACE PROCEDURE insertarAlumno(id IN alumno.id%TYPE,
nombre IN alumno.nombre%TYPE, telefono IN alumno.telefono%TYPE,
email IN alumno.email%TYPE, fechaNacimiento IN alumno.fechaNacimiento%TYPE,
carrera IN alumno.carrera%TYPE)
AS
BEGIN
	INSERT INTO alumno VALUES(id,nombre,telefono,email,fechaNacimiento,carrera);
END;
/

CREATE OR REPLACE PROCEDURE modificarAlumno(idIn IN alumno.id%TYPE,
nombreNuevo IN alumno.nombre%TYPE, telefonoNuevo IN alumno.telefono%TYPE,
emailNuevo IN alumno.email%TYPE, fechaNacimientoNuevo IN alumno.fechaNacimiento%TYPE,
carreraNuevo IN alumno.carrera%TYPE)
AS
BEGIN
	UPDATE alumno 
	SET nombre=nombreNuevo, telefono=telefonoNuevo, email=emailNuevo,
	fechaNacimiento=fechaNacimientoNuevo,carrera=carreraNuevo	WHERE id=idIn;
END;
/

CREATE OR REPLACE FUNCTION buscarAlumno(idBuscar IN alumno.id%TYPE)
RETURN Types.ref_cursor 
AS 
        alumno_cursor types.ref_cursor; 
BEGIN 
  OPEN alumno_cursor FOR 
       SELECT id,nombre,telefono,email,fechaNacimiento,carrera FROM alumno WHERE id=idBuscar; 
RETURN alumno_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarAlumnos
RETURN Types.ref_cursor 
AS 
        alumno_cursor types.ref_cursor; 
BEGIN 
  OPEN alumno_cursor FOR 
       SELECT id,nombre,telefono,email,fechaNacimiento,carrera FROM alumno; 
RETURN alumno_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE eliminarAlumno(idIn IN alumno.id%TYPE)
AS
BEGIN
    DELETE FROM alumno WHERE id=idIn;
END;
/

PROMPT ======================Grupos==========================
CREATE TABLE grupo(
numeroGrupo int,
ciclo int,
curso VARCHAR(20),
horario VARCHAR(20),
profesor VARCHAR(20),
CONSTRAINTS pkGrupo PRIMARY KEY (numeroGrupo),
CONSTRAINTS FKCiclo FOREIGN KEY (ciclo) REFERENCES ciclo(id),
CONSTRAINTS FKCurso FOREIGN KEY (curso) REFERENCES curso(id),
CONSTRAINTS FKProfesor FOREIGN KEY (profesor) REFERENCES profesor(id)
);

create sequence sec_grupo start with 1;

CREATE OR REPLACE PROCEDURE insertarGrupo(
ciclo IN grupo.ciclo%TYPE, curso IN grupo.curso%TYPE,
horario IN grupo.horario%TYPE, profesor IN grupo.profesor%TYPE)
AS
BEGIN
	INSERT INTO grupo VALUES(sec_grupo.NEXTVAL,ciclo,curso,horario,profesor);
END;
/

CREATE OR REPLACE PROCEDURE modificarGrupo(numeroGrupoIn IN grupo.numeroGrupo%TYPE,
cicloNuevo IN grupo.ciclo%TYPE, cursoNuevo IN grupo.curso%TYPE,
horarioNuevo IN grupo.horario%TYPE, profesorNuevo IN grupo.profesor%TYPE)
AS
BEGIN
	UPDATE grupo 
	SET ciclo=cicloNuevo, curso=cursoNuevo, horario=horarioNuevo,
	profesor=profesorNuevo WHERE numeroGrupo=numeroGrupoIn;
END;
/

CREATE OR REPLACE FUNCTION buscarGrupo(numeroGrupoBuscar IN grupo.numeroGrupo%TYPE)
RETURN Types.ref_cursor 
AS 
        grupo_cursor types.ref_cursor; 
BEGIN 
  OPEN grupo_cursor FOR 
       SELECT numeroGrupo,ciclo,curso,horario,profesor FROM grupo WHERE numeroGrupo=numeroGrupoBuscar; 
RETURN grupo_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarGrupos
RETURN Types.ref_cursor 
AS 
        grupo_cursor types.ref_cursor; 
BEGIN 
  OPEN grupo_cursor FOR 
       SELECT numeroGrupo,ciclo,curso,horario,profesor FROM grupo; 
RETURN grupo_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE eliminarGrupo(numeroGrupoIn IN grupo.numeroGrupo%TYPE)
AS
BEGIN
    DELETE FROM grupo WHERE numeroGrupo=numeroGrupoIn;
END;
/

PROMPT ======================Matricula==========================
--drop table matricula;
CREATE TABLE matricula(
id int,
alumno int,
grupo int,
nota int,
CONSTRAINTS pkMatricula PRIMARY KEY (id),
CONSTRAINTS FKEstudiante FOREIGN KEY (alumno) REFERENCES alumno(id),
CONSTRAINTS FKGrupo FOREIGN KEY (grupo) REFERENCES grupo(numeroGrupo)
);

create sequence sec_matricula start with 1;

CREATE OR REPLACE PROCEDURE insertarMatricula(
alumno IN matricula.alumno%TYPE, grupo IN matricula.grupo%TYPE,
nota IN matricula.nota%TYPE)
AS
BEGIN
	INSERT INTO matricula VALUES(sec_matricula.NEXTVAL,alumno,grupo,nota);
END;
/

CREATE OR REPLACE PROCEDURE modificarMatricula(idIn IN matricula.id%TYPE,
alumnoNuevo IN matricula.alumno%TYPE, grupoNuevo IN matricula.grupo%TYPE,
notaNuevo IN matricula.nota%TYPE)
AS
BEGIN
	UPDATE matricula 
	SET alumno=alumnoNuevo, grupo=grupoNuevo, nota=notaNuevo WHERE id=idIn;
END;
/

CREATE OR REPLACE FUNCTION buscarMatricula(idBuscar IN matricula.id%TYPE)
RETURN Types.ref_cursor 
AS 
        matricula_cursor types.ref_cursor; 
BEGIN 
  OPEN matricula_cursor FOR 
       SELECT id,alumno,grupo,nota FROM matricula WHERE id=idBuscar; 
RETURN matricula_cursor; 
END;
/

CREATE OR REPLACE FUNCTION listarMatricula
RETURN Types.ref_cursor 
AS 
        matricula_cursor types.ref_cursor; 
BEGIN 
  OPEN matricula_cursor FOR 
       SELECT id,alumno,grupo,nota FROM matricula; 
RETURN matricula_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE eliminarMatricula(idIn IN matricula.id%TYPE)
AS
BEGIN
    DELETE FROM matricula WHERE id=idIn;
END;
/

PROMPT ======================David==========================

-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@ CURSO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@

create  table curso (id VARCHAR(20), nombre VARCHAR(50), creditos int, horas int, constraints pkcurso PRIMARY KEY (id) );

create or replace procedure insertarCurso(id in curso.id%TYPE, nombre in curso.nombre%TYPE,
creditos in curso.creditos%TYPE, horas in curso.horas%TYPE)
AS
BEGIN
    insert into curso values(id,nombre,creditos,horas);
END;
/

create or replace procedure eliminarCurso(idBuscar in curso.id%TYPE)
AS
BEGIN
    delete from curso where id=idBuscar;
END;
/

create or replace procedure modificarCurso(ids in curso.id%TYPE, nuevoNombre in curso.nombre%TYPE, 
nuevoCreditos in curso.creditos%TYPE,
nuevoHoras in curso.horas%TYPE)
AS
BEGIN
    UPDATE curso set nombre = nuevoNombre, creditos = nuevoCreditos, horas = nuevoHoras where id = ids;
END;
/

create or replace function buscarCurso(idBuscar in curso.id%TYPE)
return types.ref_cursor
as
    curso_cursor types.ref_cursor;
begin
    
    open curso_cursor for 
        select id,nombre,creditos,horas from curso where id=idBuscar;
    return curso_cursor;
end buscarCurso;
/


create or replace function listarCursos
return Types.ref_cursor
as
    curso_cursor types.ref_cursor;
begin
    open curso_cursor for
    select id,nombre,creditos,horas from curso;
return curso_cursor;
end;
/

-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@ profesor @@@@@@@@@@@@@@@@@@@@@@@@@@@@@

-- @@@@@@@@@@@@@@@@@@@@@@@@@@@@ PROFESOR @@@@@@@@@@@@@@@@@@@@@@@@@@@@@

 create  table profesor (id VARCHAR(20), nombre VARCHAR(50), telefono int, email VARCHAR(100), constraints pkprofesor PRIMARY KEY (id) );

create or replace procedure insertarProfesor(id in profesor.id%TYPE, nombre in profesor.nombre%TYPE,
telefono in profesor.telefono%TYPE, email in profesor.email%TYPE)
AS
BEGIN
    insert into profesor values(id,nombre,telefono,email);
    commit;
END;
/

create or replace procedure eliminarProfesor(idBuscar in profesor.id%TYPE)
AS
BEGIN
    delete from profesor where id=idBuscar;
END;
/

create or replace procedure modificarProfesor(ids in profesor.id%TYPE, nuevoNombre in profesor.nombre%TYPE, 
nuevoTelefono in profesor.telefono%TYPE,
nuevoEmail in profesor.email%TYPE)
AS
BEGIN
    UPDATE profesor set nombre = nuevoNombre, telefono = nuevoTelefono, email = nuevoEmail where id = ids;
END;
/

create or replace function buscarProfesor(idBuscar in profesor.id%TYPE)
return types.ref_cursor
as
    profesor_cursor types.ref_cursor;
begin
    
    open profesor_cursor for 
        select id,nombre,telefono,email from profesor where id=idBuscar;
    return profesor_cursor;
end;
/

create or replace function listarProfesores
return Types.ref_cursor
as
    profesor_cursor types.ref_cursor;
begin
    open profesor_cursor for
    select id,nombre,telefono,email from profesor;
return profesor_cursor;
end;
/