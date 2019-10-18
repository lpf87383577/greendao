package com.shinhoandroid.test0909.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;
import com.shinhoandroid.test0909.db.DaoSession;
import com.shinhoandroid.test0909.db.IdCardDao;
import com.shinhoandroid.test0909.db.StudentDao;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;
import com.shinhoandroid.test0909.db.CreditCardDao;
import com.shinhoandroid.test0909.db.TeacherDao;

/**
 * @author Liupengfei
 * @describe 学生的实体类
 * @date on 2019/10/15 10:47
 */
@Entity
public class Student {

    @Id(autoincrement = true)
    Long id;

    @Unique
    Long studentNo;//学号

    int age; //年龄

    String sex; //性别

    String name;//姓名

    //设置关联外键 用studentNo和IdCard这张表进行关联,正常是以主键做关联
    @ToOne(joinProperty = "studentNo")
    IdCard mIdCard;

    //设置关联外键，指定与之关联的其他类的键
    @ToMany(referencedJoinProperty = "id")
    List<CreditCard> mCreditCards;


    @ToMany
    @JoinEntity(entity = StudentAndTeacherBean.class,sourceProperty = "studentNo",targetProperty = "teacherId")
    List<Teacher> teacherList;


    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1943931642)
    private transient StudentDao myDao;

    @Generated(hash = 1373181482)
    public Student(Long id, Long studentNo, int age, String sex, String name) {
        this.id = id;
        this.studentNo = studentNo;
        this.age = age;
        this.sex = sex;
        this.name = name;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentNo() {
        return this.studentNo;
    }

    public void setStudentNo(Long studentNo) {
        this.studentNo = studentNo;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Generated(hash = 709993798)
    private transient Long mIdCard__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1327326067)
    public IdCard getMIdCard() {
        Long __key = this.studentNo;
        if (mIdCard__resolvedKey == null || !mIdCard__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IdCardDao targetDao = daoSession.getIdCardDao();
            IdCard mIdCardNew = targetDao.load(__key);
            synchronized (this) {
                mIdCard = mIdCardNew;
                mIdCard__resolvedKey = __key;
            }
        }
        return mIdCard;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 164090821)
    public void setMIdCard(IdCard mIdCard) {
        synchronized (this) {
            this.mIdCard = mIdCard;
            studentNo = mIdCard == null ? null : mIdCard.getIdNo();
            mIdCard__resolvedKey = studentNo;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1701634981)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStudentDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2132506471)
    public List<CreditCard> getMCreditCards() {
        if (mCreditCards == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CreditCardDao targetDao = daoSession.getCreditCardDao();
            List<CreditCard> mCreditCardsNew = targetDao._queryStudent_MCreditCards(id);
            synchronized (this) {
                if (mCreditCards == null) {
                    mCreditCards = mCreditCardsNew;
                }
            }
        }
        return mCreditCards;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1035226502)
    public synchronized void resetMCreditCards() {
        mCreditCards = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1986556941)
    public List<Teacher> getTeacherList() {
        if (teacherList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TeacherDao targetDao = daoSession.getTeacherDao();
            List<Teacher> teacherListNew = targetDao._queryStudent_TeacherList(id);
            synchronized (this) {
                if (teacherList == null) {
                    teacherList = teacherListNew;
                }
            }
        }
        return teacherList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 973821661)
    public synchronized void resetTeacherList() {
        teacherList = null;
    }

  


}
