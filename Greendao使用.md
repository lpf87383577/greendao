#### 导入Gradle插件和Dao代码生成

要在Android项目中使用GreenDao，您需要添加GreenDao Gradle插件并添加GreenDao库：

1、导入插件
```
// 在 Project的build.gradle 文件中添加:
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' // add plugin
    }
}

```
2、配置相关依赖
```
apply from: 'greendao.gradle'

dependencies {
    //greendao
    implementation 'org.greenrobot:greendao:3.2.2'
}

```
3、配置数据库相关信息
```
apply plugin: 'org.greenrobot.greendao' // apply plugin

greendao {
    schemaVersion 1 //数据库版本
    targetGenDir 'src/main/java' //指定生成代码的目录
    daoPackage 'com.shinhoandroid.test0909.db' //包名+代码生成目录
    //generateTests false //设置为true以自动生成单元测试。
    //targetGenDirTests 'src/main/java' //应存储生成的单元测试的基本目录。默认为 src / androidTest / java。
}

```
####  创建存储对象实体类
使用GreenDao存储数据只需要在存储数据类前面声明@Entity注解就让GreenDao为其生成必要的代码：

==设置主键时使用Long类型，不能使用long类型==

```
@Entity
public class User {

    String userName;
    //@Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
    long id;
    
    String gender;
}
```
####  GreenDao初始化

```
public class MyApplication extends Application {

    private static MyApplication mApp;
    private static DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        //配置数据库
        initGreenDao();
    }

    private void initGreenDao() {
        //创建数据库mydb.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mApp,"mydb.db");
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getmDaoSession(){
        return mDaoSession;
    }
}

```
初始化完成之后重新rebuild一下项目会发现在设置的targetGenDir的目录生成三个类文件，这个是GreenDao自动生成的！说明数据库已经连接好了，咱们接下来只需要进行数据库的增删改查操作就行了

#### 使用GreenDao实现增删改查

1、增删改
```
    //插入数据
    public static void insertData(Object s) {
        MyApplication.getmDaoSession().insert(s);
    }

    //数据存在则替换，数据不存在则插入
    public static void insertOrReplace(Object s) {
        MyApplication.getmDaoSession().insertOrReplace(s);
    }

    //删除单一数据（按照主键去删除）
    public static void deleteData(Object s) {
        MyApplication.getmDaoSession().delete(s);
    }

    //删除整个表数据
    public static void deleteAll(Class c) {

        MyApplication.getmDaoSession().deleteAll(c);
    }

    //更新
    public static void updataData(Object s) {

        MyApplication.getmDaoSession().update(s);
    }

```
2、查

loadAll()：查询所有数据。

queryRaw()：根据条件查询。

queryBuilder() : 方便查询的创建，后面详细讲解。

```
//查询所有数据
public List queryAll(){
        List<Student> students = daoSession.loadAll(Student.class);
        return students;
    }
//通过ID查询数据    
public void queryData(String s) {
       List<Student> students = daoSession.queryRaw(Student.class, " where id = ?", s);
        mDataBaseAdapter.addNewStudentData(students);
    }    
    
```
### QueryBuilder的使

==常用方法==

where(WhereCondition cond, WhereCondition... condMore): 查询条件，参数为查询的条件！

or(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore): 嵌套条件或者，用法同or。

and(WhereCondition cond1, WhereCondition cond2, WhereCondition... condMore): 嵌套条件且，用法同and。

join(Property sourceProperty, Class<J> destinationEntityClass):多表查询，后面会讲。
输出结果有四种方式，选择其中一种最适合的即可，list()返回值是List,而其他三种返回值均实现Closeable,需要注意的不使用数据时游标的关闭操作：

list （）所有实体都加载到内存中。结果通常是一个没有魔法的 ArrayList。最容易使用。

listLazy （）实体按需加载到内存中。首次访问列表中的元素后，将加载并缓存该元素以供将来使用。必须关闭。

listLazyUncached（）实体的“虚拟”列表：对列表元素的任何访问都会导致从数据库加载其数据。必须关闭。

listIterator （）让我们通过按需加载数据（懒惰）来迭代结果。数据未缓存。必须关闭。

orderAsc() 按某个属性升序排；

orderDesc() 按某个属性降序排；

==GreenDao中SQL语句的缩写，我们也了解下，源码在Property中,使用的时候可以自己点进去查询即可：==

eq()："equal ('=?')" 等于；

notEq() ："not equal ('<>?')" 不等于；

like()：" LIKE ?" 值等于；

between()：" BETWEEN ? AND ?" 取中间范围；

in()：" IN ("  in命令;

notIn()：" NOT IN (" not in 命令;

gt()：">?"  大于;

lt()："<? "  小于;

ge()：">=?"  大于等于;

le()："<=? "  小于等于;

isNull()：" IS NULL" 为空;

isNotNull()：" IS NOT NULL" 不为空;

#### 使用QueryBuilder进行查询操作
```
//查询所有数据
public List queryAllList(){
        DaoSession daoSession = ((AserbaoApplication) getApplication()).getDaoSession();
        QueryBuilder<Student> qb = daoSession.queryBuilder(Student.class);
        List<Student> list = qb.list(); // 查出所有的数据
    return list;
    }


//根据条件查询    
public List queryListByMessage(String name){
         DaoSession daoSession = ((AserbaoApplication) getApplication()).getDaoSession();
        QueryBuilder<Student> qb = daoSession.queryBuilder(Student.class);
        QueryBuilder<Student> studentQueryBuilder = qb.where(StudentDao.Properties.Name.eq("name")).orderAsc(StudentDao.Properties.Name);
        List<Student> studentList = studentQueryBuilder.list(); //查出当前对应的数据
        return list;
    }
    
    // 查询ID大于5的所有学生
    public List queryListBySqL(){

        DaoSession daoSession = ((AserbaoApplication) getApplication()).getDaoSession();
        Query<Student> query = daoSession.queryBuilder(Student.class).where(
                new WhereCondition.StringCondition("_ID IN " + "(SELECT _ID FROM STUDENT WHERE _ID > 5)")
        ).build();
        List<Student> list = query.list();
        return list;
    }
    
    
//查询Id大于5小于10，且Name值为"一"
public List queryList(){
        DaoSession daoSession = ((AserbaoApplication) getApplication()).getDaoSession();
        QueryBuilder<Student> qb = daoSession.queryBuilder(Student.class);
        qb = daoSession.queryBuilder(Student.class);
        List<Student> list2 = qb.where(StudentDao.Properties.Name.eq("一"),
                qb.and(StudentDao.Properties.Id.gt(5),
                        StudentDao.Properties.Id.le(50))).list();
        return  list2;
    }    
```
#### 使用QueryBuilder进行批量删除操作
```
//删除数据库中id大于5的所有其他数据
public boolean deleteItem(){
        DaoSession daoSession = ((AserbaoApplication) getApplication()).getDaoSession();
        QueryBuilder<Student> where = daoSession.queryBuilder(Student.class).where(StudentDao.Properties.Id.gt(5));
        DeleteQuery<Student> deleteQuery = where.buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        return false;
    }
```
### 注解讲解

##### 基本注解
```
@Id注解选择 long / Long属性作为实体ID。在数据库方面，它是主键。参数autoincrement = true 表示自增

@Property (nameInDb="name") //设置了，数据库中的表格属性名为"name",如果不设置，数据库中表格属性名为"NAME"
String name;

@NotNull ：设置数据库表当前列不能为空 。

@Transient ：添加此标记之后不会生成数据库表的列

@Index：使用@Index作为一个属性来创建一个索引，通过name设置索引别名，也可以通过unique给索引添加约束。
@Unique：向索引添加UNIQUE约束，强制所有值都是唯一的。

```
##### 关系注解

```
@ToOne：定义与另一个实体（一个实体对象）的关系
@ToMany：定义与多个实体对象的关系

```
 一对一
 
比如：一个人对应一个身份证
```
@Entity
public class Student {
     @Id(autoincrement = true)
    Long id;

    @Unique
    Long studentNo;//学号

    int age; //年龄

    String sex; //性别

    String name;//姓名

    //设置关联外键 用studentNo和IdCard这张表进行关联,正常是和从表的主键做关联，数据类型必须一致
    @ToOne(joinProperty = "studentNo")
    IdCard mIdCard;
    
}

@Entity
public class IdCard {
  
    @Id
    Long idNo;//学生号

    String userName;//用户名
   
}

//插入数据（两个表分别存储即可）
public void addStudent(){
       DaoSession daoSession = MyApplication.getmDaoSession();

        Student student = new Student();

        student.setStudentNo(onetwoone);

        student.setAge(12);

        student.setName("小"+onetwoone);

        daoSession.insert(student);

        //插入对应的IdCard数据
        IdCard idCard = new IdCard();
        idCard.setUserName("小"+onetwoone);
        idCard.setIdNo(onetwoone);
        daoSession.insert(idCard);
                 
      }

```
 一对多
 
 比如：一个人拥有多个信用卡

==因为是一对多的关系，那相对从表的键就不能是主键，也不能是唯一的==
```
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

      
    }
    
    
    @Entity
public class CreditCard {

    Long id;  //身份证号码

    String userName;//持有者名字

    String cardNum;//卡号


    }
  
  //插入数据
  public void addStudent(){
         DaoSession daoSession = MyApplication.getmDaoSession();
        Student student = new Student();
        student.setId(onetwoMany);

        student.setAge(1);
        student.setName("小明"+onetwoMany);
        daoSession.insert(student);

        //插入对应的CreditCard数据
        for (int j = 0; j<5 ; j++) {

            CreditCard creditCard = new CreditCard();

            creditCard.setId(onetwoMany);

            creditCard.setCardNum("111"+onetwoMany+""+j);

            daoSession.insert(creditCard);
        }
        onetwoMany++;               
                  
      }
    
```
多对多

比如：一个学生有多个老师，老师有多个学生。

```
//我们需要创建一个学生老师管理器(StudentAndTeacherBean)，用来对应学生和老师的ID;
@Entity
public class StudentAndTeacherBean {
    @Id(autoincrement = true)
    Long id;
    Long studentId;//学生ID
    Long teacherId;//老师ID
    
}


//我们需要在学生对象中，添加注解：
@ToMany
@JoinEntity(entity = StudentAndTeacherBean.class,sourceProperty = "studentId",targetProperty = "teacherId")
List<Teacher> teacherList;


//我们需要在老师对象中，添加注解：
@ToMany
 @JoinEntity(entity = StudentAndTeacherBean.class,sourceProperty = "teacherId",targetProperty = "studentId")
    List<Student> studentList;
    
//添加数据
public void addData(){
                       Student student = new Student();
                        student.setStudentNo(i);
                        int age = mRandom.nextInt(10) + 10;
                        student.setAge(age);
                        student.setTelPhone(RandomValue.getTel());
                        String chineseName = RandomValue.getChineseName();
                        student.setName(chineseName);
                        daoSession.insert(student);

                        Collections.shuffle(teacherList);
                        for (int j = 0; j < mRandom.nextInt(8) + 1; j++) {
                            if(j < teacherList.size()){
                                Teacher teacher = teacherList.get(j);
                                StudentAndTeacherBean teacherBean = new StudentAndTeacherBean(student.getId(), teacher.getId());
                                daoSession.insert(teacherBean);
                            }
                        }
                        }    
    
```

### 数据库的升级

GreenDao的OpenHelper下有个 onUpgrade(Database db, int oldVersion, int newVersion)方法，当设置的数据库版本改变时，在数据库初始化的时候就会回调到这个方法，我们可以通过继承OpenHelper重写onUpgrade方法来实现数据库更新操作：

GreenDao的升级思路：

创建临时表TMP_,复制原来的数据库到临时表中；

删除之前的原表；

创建新表；

将临时表中的数据复制到新表中，最后将TMP_表删除掉；

==参考博客：https://github.com/yuweiguocn/GreenDaoUpgradeHelper/blob/master/README_CH.md==
```
public class MyDaoMaster extends OpenHelper {
    private static final String TAG = "MyDaoMaster";
    public MyDaoMaster(Context context, String name) {
        super(context, name);
    }

    public MyDaoMaster(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },ThingDao.class);
        Log.e(TAG, "onUpgrade: " + oldVersion + " newVersion = " + newVersion);
    }
}


public final class MigrationHelper {

    public static boolean DEBUG = false;
    private static String TAG = "MigrationHelper";
    private static final String SQLITE_MASTER = "sqlite_master";
    private static final String SQLITE_TEMP_MASTER = "sqlite_temp_master";

    private static WeakReference<ReCreateAllTableListener> weakListener;

    public interface ReCreateAllTableListener{
        void onCreateAllTables(Database db, boolean ifNotExists);
        void onDropAllTables(Database db, boolean ifExists);
    }

    public static void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【The Old Database Version】" + db.getVersion());
        Database database = new StandardDatabase(db);
        migrate(database, daoClasses);
    }

    public static void migrate(SQLiteDatabase db, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(db, daoClasses);
    }

    public static void migrate(Database database, ReCreateAllTableListener listener, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        weakListener = new WeakReference<>(listener);
        migrate(database, daoClasses);
    }

    public static void migrate(Database database, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        printLog("【Generate temp table】start");
        generateTempTables(database, daoClasses);
        printLog("【Generate temp table】complete");

        ReCreateAllTableListener listener = null;
        if (weakListener != null) {
            listener = weakListener.get();
        }

        if (listener != null) {
            listener.onDropAllTables(database, true);
            printLog("【Drop all table by listener】");
            listener.onCreateAllTables(database, false);
            printLog("【Create all table by listener】");
        } else {
            dropAllTables(database, true, daoClasses);
            createAllTables(database, false, daoClasses);
        }
        printLog("【Restore data】start");
        restoreData(database, daoClasses);
        printLog("【Restore data】complete");
    }

    private static void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            String tempTableName = null;

            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            if (!isTableExists(db, false, tableName)) {
                printLog("【New Table】" + tableName);
                continue;
            }
            try {
                tempTableName = daoConfig.tablename.concat("_TEMP");
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";");
                db.execSQL(dropTableStringBuilder.toString());

                StringBuilder insertTableStringBuilder = new StringBuilder();
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName);
                insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";");
                db.execSQL(insertTableStringBuilder.toString());
                printLog("【Table】" + tableName +"\n ---Columns-->"+getColumnsStr(daoConfig));
                printLog("【Generate temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to generate temp table】" + tempTableName, e);
            }
        }
    }

    private static boolean isTableExists(Database db, boolean isTemp, String tableName) {
        if (db == null || TextUtils.isEmpty(tableName)) {
            return false;
        }
        String dbName = isTemp ? SQLITE_TEMP_MASTER : SQLITE_MASTER;
        String sql = "SELECT COUNT(*) FROM " + dbName + " WHERE type = ? AND name = ?";
        Cursor cursor=null;
        int count = 0;
        try {
            cursor = db.rawQuery(sql, new String[]{"table", tableName});
            if (cursor == null || !cursor.moveToFirst()) {
                return false;
            }
            count = cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return count > 0;
    }


    private static String getColumnsStr(DaoConfig daoConfig) {
        if (daoConfig == null) {
            return "no columns";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < daoConfig.allColumns.length; i++) {
            builder.append(daoConfig.allColumns[i]);
            builder.append(",");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }


    private static void dropAllTables(Database db, boolean ifExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "dropTable", ifExists, daoClasses);
        printLog("【Drop all table by reflect】");
    }

    private static void createAllTables(Database db, boolean ifNotExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        reflectMethod(db, "createTable", ifNotExists, daoClasses);
        printLog("【Create all table by reflect】");
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private static void reflectMethod(Database db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses.length < 1) {
            return;
        }
        try {
            for (Class cls : daoClasses) {
                Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
                method.invoke(null, db, isExists);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");

            if (!isTableExists(db, true, tempTableName)) {
                continue;
            }

            try {
                // get all columns from tempTable, take careful to use the columns list
                List<TableInfo> newTableInfos = TableInfo.getTableInfo(db, tableName);
                List<TableInfo> tempTableInfos = TableInfo.getTableInfo(db, tempTableName);
                ArrayList<String> selectColumns = new ArrayList<>(newTableInfos.size());
                ArrayList<String> intoColumns = new ArrayList<>(newTableInfos.size());
                for (TableInfo tableInfo : tempTableInfos) {
                    if (newTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);
                        selectColumns.add(column);
                    }
                }
                // NOT NULL columns list
                for (TableInfo tableInfo : newTableInfos) {
                    if (tableInfo.notnull && !tempTableInfos.contains(tableInfo)) {
                        String column = '`' + tableInfo.name + '`';
                        intoColumns.add(column);

                        String value;
                        if (tableInfo.dfltValue != null) {
                            value = "'" + tableInfo.dfltValue + "' AS ";
                        } else {
                            value = "'' AS ";
                        }
                        selectColumns.add(value + column);
                    }
                }

                if (intoColumns.size() != 0) {
                    StringBuilder insertTableStringBuilder = new StringBuilder();
                    insertTableStringBuilder.append("REPLACE INTO ").append(tableName).append(" (");
                    insertTableStringBuilder.append(TextUtils.join(",", intoColumns));
                    insertTableStringBuilder.append(") SELECT ");
                    insertTableStringBuilder.append(TextUtils.join(",", selectColumns));
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
                    db.execSQL(insertTableStringBuilder.toString());
                    printLog("【Restore data】 to " + tableName);
                }
                StringBuilder dropTableStringBuilder = new StringBuilder();
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
                db.execSQL(dropTableStringBuilder.toString());
                printLog("【Drop temp table】" + tempTableName);
            } catch (SQLException e) {
                Log.e(TAG, "【Failed to restore data from temp table 】" + tempTableName, e);
            }
        }
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", null);
            if (null != cursor && cursor.getColumnCount() > 0) {
                columns = Arrays.asList(cursor.getColumnNames());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (null == columns)
                columns = new ArrayList<>();
        }
        return columns;
    }

    private static void printLog(String info){
        if(DEBUG){
            Log.d(TAG, info);
        }
    }

    private static class TableInfo {
        int cid;
        String name;
        String type;
        boolean notnull;
        String dfltValue;
        boolean pk;

        @Override
        public boolean equals(Object o) {
            return this == o
                    || o != null
                    && getClass() == o.getClass()
                    && name.equals(((TableInfo) o).name);
        }

        @Override
        public String toString() {
            return "TableInfo{" +
                    "cid=" + cid +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", notnull=" + notnull +
                    ", dfltValue='" + dfltValue + '\'' +
                    ", pk=" + pk +
                    '}';
        }

        private static List<TableInfo> getTableInfo(Database db, String tableName) {
            String sql = "PRAGMA table_info(" + tableName + ")";
            printLog(sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor == null)
                return new ArrayList<>();
            TableInfo tableInfo;
            List<TableInfo> tableInfos = new ArrayList<>();
            while (cursor.moveToNext()) {
                tableInfo = new TableInfo();
                tableInfo.cid = cursor.getInt(0);
                tableInfo.name = cursor.getString(1);
                tableInfo.type = cursor.getString(2);
                tableInfo.notnull = cursor.getInt(3) == 1;
                tableInfo.dfltValue = cursor.getString(4);
                tableInfo.pk = cursor.getInt(5) == 1;
                tableInfos.add(tableInfo);
                // printLog(tableName + "：" + tableInfo);
            }
            cursor.close();
            return tableInfos;
        }
    }
}


```

#### GreenDao数据库加密
```
//导入加密库文件
implementation 'net.zetetic:android-database-sqlcipher:3.5.6'

//修改DaoSession的生成方式：

//       MyDaoMaster helper = new MyDaoMaster(this, "aserbaos.db");  //数据库升级写法
DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "aserbao.db");
        //SQLiteDatabase db = helper.getWritableDatabase(); //不加密的写法
        Database db = helper.getEncryptedWritableDb("aserbao"); //数据库加密密码为“aserbao"的写法
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
```
```

```
```

```
```

```