package planning.repository;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import planning.exception.InternalServerException;
import planning.message.CommonMessage;
import planning.model.Classroom;
import planning.modelVO.ClassroomVO;
import java.util.List;

@Repository
public class ClassroomCRUD {

    private final SessionFactory hibernate;

    @Autowired
    public ClassroomCRUD(SessionFactory hibernate) {
        this.hibernate = hibernate;
    }

    @Transactional
    public Classroom getClassroomByName(String className) {
        try {
            Session session = hibernate.getCurrentSession();

            return session.createQuery("FROM Classroom where name = :name", Classroom.class)
                    .setParameter("name", className)
                    .uniqueResult();

        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    @Transactional
    public Classroom addClassroom(ClassroomVO classroomVO){
        Classroom classroom = new Classroom();
        classroom.setName(classroomVO.getName());

        try {
            Session session = hibernate.getCurrentSession();

            session.save(classroom);

            return classroom;
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public List<Classroom> getAllClassrooms(){
        try{
            Session session = hibernate.getCurrentSession();

            return session.createQuery("from Classroom " +
                    "where removed = false " +
                    "order by name", Classroom.class)
                    .list();

        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public void deleteClassroom(Classroom classroom) {
        classroom.setRemoved(true);

        try {
            Session session = hibernate.getCurrentSession();

            session.update(classroom);
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public Classroom getClassroomById(long classId) {
        try{
            Session session = hibernate.getCurrentSession();

            return session.createQuery("from Classroom where id = :classId and removed = false", Classroom.class)
                    .setParameter("classId", classId)
                    .uniqueResult();
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public Classroom checkDuplicateClassName(long classId, String className) {
        try{
            Session session = hibernate.getCurrentSession();

            return session.createQuery("from Classroom " +
                    "where id != :classId " +
                    "and name = :className " +
                    "and removed = false", Classroom.class)
                    .setParameter("classId", classId)
                    .setParameter("className", className)
                    .uniqueResult();
        } catch (HibernateException ex) {
            throw InternalServerException.getInstance(CommonMessage.getHibernateError());
        }
    }

    public Classroom updateClassroom(Classroom classroom, ClassroomVO classroomVO) {
        if(classroomVO != null) {
            classroom.setName(classroom.getName());

            try {
                Session session = hibernate.getCurrentSession();

                session.update(classroom);

                return classroom;
            } catch (HibernateException ex) {
                throw InternalServerException.getInstance(CommonMessage.getHibernateError());
            }
        }

        return null;
    }
}
