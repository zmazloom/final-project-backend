package planning.repository;

import java.util.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import planning.exception.InternalServerException;
import planning.message.CommonMessage;
import planning.model.Teacher;

@Repository
public class TeacherCRUD {

    private final SessionFactory hibernate;

    @Autowired
    public TeacherCRUD(SessionFactory hibernate) {
        this.hibernate = hibernate;
    }

    public List<Teacher> getAllTeachers() {
        try {
            Session session = hibernate.getCurrentSession();

            return session.createQuery("FROM Teacher " +
                    "where removed = false " +
                    "order by lastName", Teacher.class)
                    .list();
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public Teacher getTeacherById(long id) {
        try {
            Session session = hibernate.getCurrentSession();

            return session.createQuery("from Teacher where id = :id", Teacher.class)
                    .setParameter("id", id)
                    .uniqueResult();

        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public void deleteTeacher(Teacher teacher) {
        teacher.setRemoved(true);

        try {
            Session session = hibernate.getCurrentSession();
            session.update(teacher);
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

}