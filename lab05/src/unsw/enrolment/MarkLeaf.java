package unsw.enrolment;

public class MarkLeaf implements MarkComponent{
    private String name; 
	private double mark;
    private double max_mark;
	private Enrolment enrol;

	public MarkLeaf(String name, double mark, double max_mark) {
		super();
		this.name = name;
        this.mark = mark;
		this.max_mark = max_mark;
		this.enrol = null;
	}

	@Override
	public double AverageMark() {
		return this.mark;
	}

	@Override 
	public double SumMark() {
		return this.mark;
    }

    @Override 
	public String getName() {
		return name;
	}

	public void setEnrol(Enrolment enrol) {
		this.enrol = enrol;
	}

	public Enrolment getEnrol( ) {
		return enrol;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getmark() {
		return mark;
	}

	public void setmark(double mark) {
		this.mark = mark;
    }
    
	public double getMaxmark() {
		return max_mark;
	}
}
