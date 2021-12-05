package geek.week5;

public class CustomizeAutowired {

    private Student student;

    //注解装配bean
//    StudentService service;

    public CustomizeAutowired(){
    }

    public CustomizeAutowired(Student student){
        super();
        this.student = student;
    }
    public Student getStudent(){
        return student;
    }
    public void setStudent(){
        this.student = new Student("lily", "11");
    }
}
