package unsw.skydiving;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Flight {
    private String id;
    private int maxload;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
	private String dropzone;
	private List<Fun_jump> fun_jumps;
	private List<Training_jump> training_jumps;
    private List<Tandem_jump> tandem_jumps;

    public  Flight(String id, int maxload, LocalDateTime starttime, LocalDateTime endtime, String dropzone) {
		this.id = id;
		this.maxload = maxload;
		this.starttime = starttime;
		this.endtime = endtime;
		this.dropzone = dropzone;
		fun_jumps = new ArrayList<Fun_jump>();
		training_jumps = new ArrayList<Training_jump>();
		tandem_jumps = new ArrayList<Tandem_jump>();
    }
    
    public String getId() {
		return id;
	}

	public Integer getMaxload() {
		return maxload;
	}

	public LocalDateTime getStarttime() {
		return starttime;
	}

	public LocalDate getStarttime_day() {
		return starttime.toLocalDate();
	}

	public LocalDateTime getEndtime() {
		return endtime;
    }
    
	public String getDropzone() {
		return dropzone;
	}

	public void addFun_jump(Fun_jump fun_jump) {
        fun_jumps.add(fun_jump);
	}

	public void addTraining_jump(Training_jump training_jump) {
        training_jumps.add(training_jump);
	}
	
	public void addTandem_jump(Tandem_jump tandem_jump) {
       tandem_jumps.add(tandem_jump);
    }
	
	public int getVacanciesSize() {
		int tmp = 0;
		for(int i = 0; i < fun_jumps.size(); i++) {
			tmp += fun_jumps.get(i).numof_skydivers();
		}
		return maxload - tmp - 2 * training_jumps.size() - 2 * tandem_jumps.size() ;
	}

	/**
	 * find jumps in this flight with given id and cancel it
	 * @param cancel_id
	 */
	public void cancel(String cancel_id) {
		for(int i = 0; i < fun_jumps.size(); i++) {
			if (fun_jumps.get(i).getId().equals(cancel_id)){
				fun_jumps.get(i).remove_busytime();
				fun_jumps.remove(i);
				return;
			}
		}
		for(int i = 0; i < training_jumps.size(); i++) {
			if (training_jumps.get(i).getId().equals(cancel_id)){
				training_jumps.get(i).remove_busytime();
				training_jumps.remove(i);
				return;
			}
		}
		for(int i = 0; i < tandem_jumps.size(); i++) {
			if (tandem_jumps.get(i).getId().equals(cancel_id)){
				tandem_jumps.get(i).remove_busytime();
				tandem_jumps.remove(i);
				return;
			}
		}

	}
	
	/**
	 * return fun_jumps on this flight as JSONObject
	 */
	public List<JSONObject> fun_jump_run(){
		List<JSONObject> fun_jump_run_skydivers = new ArrayList<JSONObject>();
		JSONObject fun_jump_run_skydiver = new JSONObject();
		fun_jumps.sort(new fun_jumpsorter());
		for (Fun_jump fun_jump : fun_jumps) {
			fun_jump_run_skydiver.put("skydivers", fun_jump.getSkydivers());
			fun_jump_run_skydivers.add(fun_jump_run_skydiver);
		}

		return fun_jump_run_skydivers;
	}

	/**
	 * return training_jumps on this flight as JSONObject
	 */
	public List<JSONObject> training_run(){
		List<JSONObject> training_run_skydivers = new ArrayList<JSONObject>();
		JSONObject training_run_skydiver = new JSONObject();
		for (Training_jump training_jump : training_jumps) {
			training_run_skydiver.put("trainee", training_jump.getTrainee_name());
			training_run_skydiver.put("instructor", training_jump.getInstructor_name());
			training_run_skydivers.add(training_run_skydiver);
		}

		return training_run_skydivers;

	}

		/**
	 * return ttandem_jumps on this flight as JSONObject
	 */
	public List<JSONObject> tandem_run(){
		List<JSONObject> tandem_run_skydivers = new ArrayList<JSONObject>();
		JSONObject tandem_run_skydiver = new JSONObject();
		for (Tandem_jump tandem_jump : tandem_jumps) {
			tandem_run_skydiver.put("passenger", tandem_jump.getPassenger_name());
			tandem_run_skydiver.put("jump-master", tandem_jump.getTandem_master_name());
			tandem_run_skydivers.add(tandem_run_skydiver);
		}

		return tandem_run_skydivers;


	}

}
