package unsw.enrolment;
import java.util.ArrayList;

public class MarkComposite implements MarkComponent{
	private String name; 
	private double mark;
	private double max_mark;
	private Enrolment enrol;
	ArrayList<MarkComponent> children = new ArrayList<MarkComponent>();
	
	public MarkComposite(String name, double mark, double max_mark) {
		super();
		this.name = name;
        this.mark = mark;
		this.max_mark = max_mark;
		this.enrol = null;
    }
    
    public MarkComposite(String name) {
		super();
        this.name = name;
        this.mark = 0;
		this.max_mark = 0;
		this.enrol = null;
	}

	@Override
	public double AverageMark() {
        double answer = 0;
        int count = 0;
		for(MarkComponent c : children) {
            if (c.getClass() == MarkLeaf.class){
                answer += c.AverageMark();
                this.max_mark = ((MarkLeaf) c).getMaxmark();
            } else if (c.getClass() == MarkComposite.class){
                answer += ((MarkComposite) c).getMark();
                this.max_mark = ((MarkComposite) c).getMaxmark();
            }
            count++;
		}
		
		return answer/count;
	}
	
	@Override
	public double SumMark() {
		double answer = 0;
		for(MarkComponent c : children) {
            if (c.getClass() == MarkLeaf.class){
                answer += c.SumMark();
                this.max_mark += ((MarkLeaf) c).getMaxmark();
            } else if (c.getClass() == MarkComposite.class){
                answer += ((MarkComposite) c).getMark();
                this.max_mark += ((MarkComposite) c).getMaxmark();
            }

		}
		
		return answer;
	}

    @Override 
	public String getName() {
		return name;
    }
    
	public ArrayList<MarkComponent> getChildren() {
		return children;
    }

	public boolean add(MarkComponent child) {
		children.add(child);
		if (enrol != null){
			enrol.notifyObservers(child);
		}

		return true;
	}

	public boolean remove(MarkComponent child) {
		children.remove(child);
		return true;
	}

	public Enrolment getEnrol( ) {
		return enrol;
	}

	public void setEnrol(Enrolment enrol) {
		this.enrol = enrol;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMark() {
		return mark;
	}

	public void setMark(double mark) {
		this.mark = mark;
	}

    public double getMaxmark() {
		return max_mark;
	}


}