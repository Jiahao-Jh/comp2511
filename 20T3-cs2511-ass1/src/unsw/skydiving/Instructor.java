package unsw.skydiving;

public class Instructor extends Licenced_jumper{
    private String dropzone;

	public Instructor(String name, String dropzone) {
        super(name);
        this.dropzone = dropzone;
    }
    
    public String getDropzone() {
        return dropzone;
    }
    
}
