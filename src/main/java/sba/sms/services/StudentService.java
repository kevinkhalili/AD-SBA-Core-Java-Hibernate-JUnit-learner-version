package sba.sms.services;

//import jakarta.persistence.TypedQuery;
import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

//import java.util.ArrayList;
import java.util.List;

@Log
public class StudentService implements StudentI {

    @Override
    public List<Student> getAllStudents() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Student> st = s.createQuery("from Students",Student.class).list();
        s.close();
        return st;
    }

    @Override
    public void createStudent(Student student) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = s.beginTransaction();
            s.persist(student);
            tx.commit();
        } catch (HibernateException exception) {
            if (tx!=null) tx.rollback();
            exception.printStackTrace();
        } finally {
            s.close();
        }
    }
    @Override
    public Student getStudentByEmail(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            Student st = s.get(Student.class, email);
            if(tx == null)
                throw new HibernateException("Did not find email");
            else
                return st;

        } catch (HibernateException exception) {
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return new Student();
    }
    public boolean validateStudent(String email, String password) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            List <Student> studentList = getAllStudents();

            if(tx == null)
                throw new HibernateException("Did not find email");
             for(Student student : studentList){
                    if(student.getEmail().equals(email) && student.getPassword().equals(password))
                        return true;
                }
            } catch (HibernateException exception) {
            exception.printStackTrace();
        } finally {
            s.close();
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Student st = s.get(Student.class, email);
            Course c = s.get(Course.class,courseId);
            st.addCourse(c);
            s.merge(st);
            tx.commit();
        } catch (HibernateException exception) {
            if (tx!=null) tx.rollback();
            exception.printStackTrace();
        } finally {
            s.close();
        }

    }

   public List<Course> getStudentCourses(String email){

            Session s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            List<Course> courseList = null;
            try {
                tx = s.beginTransaction();
              //  TypedQuery<Course> q = s.createNamedQuery("getStudentCourses");
                NativeQuery q = s.createNativeQuery("select c.id, c.name, c.instructor from course as c join student_courses as sc on c.id = sc.courses_id join student as s on s.email = sc.student_email where s.email = :email",Course.class);
                q.setParameter("email", email);
                courseList = q.getResultList();
                tx.commit();

            } catch (HibernateException exception) {
                exception.printStackTrace();
            } finally {
                s.close();
            }
            return courseList;
        }
    public void addCourse(Course course) {
       course.addCourse(course);
      course.getCourses().add(course);

   }
}


