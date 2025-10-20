package com.korealm.simbache.repositories;

import com.korealm.simbache.models.SessionToken;
import com.korealm.simbache.models.User;
import com.korealm.simbache.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/* Los repositorios no son clases, sino que son interfaces que extienden otra interfaz, que ya trae todo hecho.
    Esta otra interfaz se llama JpaRepository, y entre los <> recibe el objeto con el que trabaja, y luego el tipo
    de la primary key.

    En este caso trabajamos con el objeto User, y con su primary key es de tipo Long.
    Es por eso que decimos UserRepository extends JpaRepository<User, Long>

    Esta interfaz genera en automático todo el CRUD para nosotros, en base a los campos que declaramos en
    el modelo (la carpeta Model).

    Si queremos definir metodos personalizados, entonces ahora si podemos definirlos adentro de la interfaz tal
    y como estamos haciendo aqui con findByUsername y findByEmail.
* */
public interface UserRepository extends JpaRepository<User, Long> {

    /*
    * Optional es un objeto en Java que puede o no contener un valor. Es la forma moderna, segura de trabajar en Java
    * Sin que nos tire un NullPointerException cuando no hay valor.
    * */

    /* Spring va a traducir este metodo en una query SQL y ejecutarla en la base de datos.
     * Para ser concreto, va a generar esta query:
            SELECT * FROM Users WHERE username = ?;
     */
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findBySessionToken(SessionToken sessionToken);

    List<User> findAllByUserRole(UserRole userRole);
    List<User> findAllByUserRole_RoleId(short roleId);
}