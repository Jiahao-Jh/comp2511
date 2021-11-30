package unsw.skydiving;


import java.time.LocalDateTime;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Skydive Booking System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a skydive booking system. Input
 * and output is in JSON format.
 *
 * @author Matthew Perry
 *
 */


public class SkydiveBookingSystem {
    private List<Flight> flights;
    private List<Student> students;
    private List<Dropzone> dropzones;
    private JSONObject result;
    private List<JSONObject> commands;
    static final int MAX_JUMPS_IN_ONE_DAY = 9999; //assume max jump times in one day is 9999
    static final int REPACK_TIME = 10;
    static final int BRIEFING_TIME = 5;
    static final int DEBRIEFING_TIME = 15;
    /**
     * Constructs a skydive booking system. Initially, the system contains no flights, skydivers, jumps or dropzones
     */
    public SkydiveBookingSystem() {
        flights = new ArrayList<Flight>();
        students = new ArrayList<Student>();
        dropzones = new ArrayList<Dropzone>();
        result = new JSONObject();
        commands = new ArrayList<JSONObject>();
    }

    private void addCommands(JSONObject json) {
        commands.add(json);
    }

    
    /**
     * add flight to List flights
     */
    private void addFlights(Flight flight) {
        flights.add(flight);
    }

    /**
     * add student to List students
     */
    private void addStudents(Student student) {
        students.add(student);
    }

    /**
     * add dropzone to List dropzones
     */
    private void addDropzones(String dropzone) {
        for(int i = 0; i < dropzones.size(); i++) {
            if (dropzones.get(i).getDropzone().equals(dropzone)){
                return;
            }
        }
        Dropzone dropzone_tobeadd = new Dropzone(dropzone);
        dropzones.add(dropzone_tobeadd);
    }

    /**
     * given dropzone name return Dropzone object
     */
    private Dropzone findDropzone(String dropzone){
        for(int i = 0; i < dropzones.size(); i++) {
            if (dropzones.get(i).getDropzone().equals(dropzone)){
                return dropzones.get(i);
            }
        }
        return null;
    }

    /**
     * given command id return full command JSONObject
     */
    private JSONObject findCommands(String id) {
        for(int i = 0; i < commands.size(); i++) {
            if(commands.get(i).getString("command").equals("request") && commands.get(i).getString("id").equals(id)){
                return commands.get(i);
            }
        }
        return null;
    }

    /**
     * given student name return Student object
     */
    private Student findStudent(String passenger_name) {
        for(int i = 0; i < students.size(); i++) {
            if (students.get(i).equal_name(passenger_name)){
                return students.get(i);
            }
        }
        return null;
    }

    /**
     *  check if student if free at given time intervel
     */
    private boolean check_student(Student student, LocalDateTime starttime, LocalDateTime endtime ) {
        return student.check_student(starttime, endtime);
    }

    /**
     * 
     * find List of flight with achieve the require on that day
     */
    private List<Flight> find_available_flights(LocalDateTime starttime, int num_of_skydivers) {
        List<Flight> found_flights = new ArrayList<Flight>();

        for(int i = 0; i < flights.size(); i++) {
            //flight with earliest starttime
            if (flights.get(i).getStarttime_day().equals(starttime.toLocalDate() ) 
                && !starttime.isAfter(flights.get(i).getStarttime()) 
                && flights.get(i).getVacanciesSize() - num_of_skydivers >= 0){
                    found_flights.add(flights.get(i));
            }
        }
        return found_flights;
    }

    /**
     * find flight with earliest starttime(may be multiple) from List of flights
     */
    private List<Flight> find_earliest_flights(List<Flight> input_flights) {
        LocalDateTime earliest_starttime = null;
        List<Flight> found_flights = new ArrayList<Flight>();

        for (Flight flight : input_flights) {
            if (earliest_starttime == null){
                earliest_starttime = flight.getStarttime();
            } 
            if (flight.getStarttime().isBefore(earliest_starttime)){
                earliest_starttime = flight.getStarttime();
                found_flights.clear();
                found_flights.add(flight);
            } else if (flight.getStarttime().equals(earliest_starttime)){
                found_flights.add(flight);
            }
        }
        return found_flights;
    }


    /**
     * 
     * @return the most appropriate flight among list of flights
     */
    private Flight find_most_appropriate_flight(List<Flight> found_flights) {
        Flight earliest_tandem_flight = null;
        if (found_flights.size() == 0){
            return null;
        } else if (found_flights.size() == 1){
            earliest_tandem_flight = found_flights.get(0);
        } else {
            //mutiple flights with same starttime
            int max = 0;
            for (Flight flight : found_flights) {
                if (findDropzone(flight.getDropzone()).getDropzone_Vacancies() > max){
                    max = findDropzone(flight.getDropzone()).getDropzone_Vacancies();
                    earliest_tandem_flight = flight;

                }
            }
        }
        return earliest_tandem_flight;
    }

    private void print_rejected(boolean isprint) {
        while(result.length()>0){
            result.remove(result.keys().next());
        }
        result.put("status", "rejected");
        if(isprint){System.out.println(result.toString(2));}
    }

    private void print_success(String id, String dropzone,boolean isprint) {
        while(result.length()>0){
            result.remove(result.keys().next());
        }
        result.put("flight", id);
        result.put("dropzone", dropzone);
        result.put("status", "success");
        if(isprint){System.out.println(result.toString(2));}
    }

    private void processCommand(JSONObject json, boolean isprint) {
        addCommands(json);
        switch (json.getString("command")) {

        case "flight":
            String id = json.getString("id");
            int maxload = json.getInt("maxload");
            LocalDateTime starttime = LocalDateTime.parse(json.getString("starttime"));
            LocalDateTime endtime = LocalDateTime.parse(json.getString("endtime"));
            String dropzone = json.getString("dropzone");
            
            // add flight
            Flight flight = new Flight(id,maxload,starttime,endtime,dropzone);
            // add dropzone if unseen
            addDropzones(dropzone);
            findDropzone(dropzone).addFlight(flight);
            addFlights(flight);
            break;
        case "skydiver":
            String licence = json.getString("licence");
            String name = json.getString("skydiver");

            switch (licence){
            case "student":
                Student skydiver = new Student(name);
                addStudents(skydiver);
                break;
            case "licenced-jumper":
                Licenced_jumper skydiver1 = new Licenced_jumper(name);
                addStudents(skydiver1);
                break;
            case "instructor":
                Instructor skydiver2 = new Instructor(name,json.getString("dropzone"));
                addDropzones(json.getString("dropzone"));
                addStudents(skydiver2);
                break;
            case "tandem-master":
                Tandem_master skydiver3 = new Tandem_master(name,json.getString("dropzone"));
                addDropzones(json.getString("dropzone"));
                addStudents(skydiver3);
                break;
            }
            break;
        case "request":
            String jump_type = json.getString("type");
            switch (jump_type){
            case "tandem":
                String tandem_jump_id = json.getString("id");
                LocalDateTime tandem_starttime = LocalDateTime.parse(json.getString("starttime"));
                String passenger_name = json.getString("passenger");

                //variable used for calcu
                tandem_starttime = tandem_starttime.plusMinutes(BRIEFING_TIME); //briefing
                Student tandem_passenger = findStudent(passenger_name);
                List<Flight> tandem_flights = new ArrayList<Flight>();
                List<Flight> satisfied_flights = new ArrayList<Flight>();
                Student tandem_master = null;   //the right tandem_master
                Flight earliest_tandem_flight = null; //the right flight
                LocalDateTime earliest_starttime = null;
                LocalDateTime earliest_endtime = null;
                int tandem_master_jump_times = MAX_JUMPS_IN_ONE_DAY;

                // find a available flight on that day
                tandem_flights = find_available_flights(tandem_starttime,2);

                //find which flight can allow passenger and tandem to go on that day
                for (Flight tandem_flight : tandem_flights) {
                    earliest_starttime = tandem_flight.getStarttime();
                    earliest_endtime = tandem_flight.getEndtime();

                    // check passenger free
                    if (!check_student(tandem_passenger, earliest_starttime.minusMinutes(BRIEFING_TIME), earliest_endtime)){
                        continue;
                    }

                    // find a free tandem master 
                    for (Student each_student : students) {
                        if (each_student.getClass() == Tandem_master.class && !each_student.equal_name(tandem_passenger.getName()) ){
                            Tandem_master tmp = (Tandem_master) each_student;
                                //ensure same dropzone
                            if( tandem_flight.getDropzone().equals(tmp.getDropzone()) 
                                //ensure tandem master  free at flight time
                            && check_student(each_student, earliest_starttime.minusMinutes(BRIEFING_TIME), earliest_endtime.plusMinutes(REPACK_TIME))  
                                //find tandem master  with less activities 
                            && each_student.getStart_time_size_on_day(earliest_starttime.toLocalDate()) < tandem_master_jump_times ){
                                tandem_master_jump_times = each_student.getStart_time_size_on_day(earliest_starttime.toLocalDate());
                                
                                tandem_master = each_student;    //keep the right master
                                satisfied_flights.add(tandem_flight); //found
                            }
                        }
                    }
                    
                }
                

                // find most appropriate flight (the dropzone with most total skydiver vacancies)
                earliest_tandem_flight = find_most_appropriate_flight(find_earliest_flights(satisfied_flights));
                if (earliest_tandem_flight == null){
                    print_rejected(isprint);
                    break;
                }

                // create tandem_jump if vaild ; mark both tandem_master and passenger busy; add to this flight
                Tandem_jump tandem_jump = new Tandem_jump(tandem_jump_id, earliest_tandem_flight, tandem_passenger, (Tandem_master)tandem_master);
                earliest_tandem_flight.addTandem_jump(tandem_jump);
                tandem_passenger.addBusytime(earliest_tandem_flight.getStarttime().minusMinutes(BRIEFING_TIME), earliest_tandem_flight.getEndtime());
                tandem_master.addBusytime(earliest_tandem_flight.getStarttime().minusMinutes(BRIEFING_TIME), earliest_tandem_flight.getEndtime().plusMinutes(REPACK_TIME));
                print_success(earliest_tandem_flight.getId(),earliest_tandem_flight.getDropzone(),isprint);
                break;

            case "training":
                String training_jump_id = json.getString("id");
                LocalDateTime training_starttime = LocalDateTime.parse(json.getString("starttime"));
                String trainee_name = json.getString("trainee");

                //variable used for calcu
                Student trainee = findStudent(trainee_name);
                List<Flight> training_flights = new ArrayList<Flight>();
                List<Flight> training_satisfied_flights = new ArrayList<Flight>();
                Student instructor = null;   //the right instructor
                Flight earliest_training_flight = null; //the right flight
                LocalDateTime earliest_training_starttime = null;
                LocalDateTime earliest_training_endtime = null;
                int instructor_jump_times = MAX_JUMPS_IN_ONE_DAY; //assume max jump times in one day is 9999

                // find a available flight
                training_flights = find_available_flights(training_starttime,2);


                //find which flight can allow passenger and tandem to go on that day
                for (Flight training_flight : training_flights) {
                    earliest_training_starttime = training_flight.getStarttime();
                    earliest_training_endtime = training_flight.getEndtime();

                    // check trainee free
                    if (!check_student(trainee, earliest_training_starttime, earliest_training_endtime.plusMinutes(DEBRIEFING_TIME))){
                        continue;
                    }

                    // find a free instructor
                    for (Student each_student : students) {
                        if (each_student instanceof Instructor && !each_student.equal_name(trainee.getName()) ){
                            Instructor tmp = (Instructor) each_student;
                                //ensure same dropzone
                            if( training_flight.getDropzone().equals(tmp.getDropzone())
                                //ensure instructor free at flight time
                            && check_student(each_student, earliest_training_starttime, earliest_training_endtime.plusMinutes(DEBRIEFING_TIME+REPACK_TIME))  
                                //find instructor with less activities 
                            && each_student.getStart_time_size_on_day(earliest_training_starttime.toLocalDate()) < instructor_jump_times ){
                                instructor_jump_times = each_student.getStart_time_size_on_day(earliest_training_starttime.toLocalDate());
                                
                                instructor = each_student;    //keep the right master
                                training_satisfied_flights.add(training_flight); //found
                            }
                        }
                    }

                }

                // the dropzone with most total skydiver vacancies
                // find most appropriate flight
                earliest_training_flight = find_most_appropriate_flight(find_earliest_flights(training_satisfied_flights));
                if (earliest_training_flight == null){
                    print_rejected(isprint);
                    break;
                }
                // create training_jump if vaild ; mark both instructor and trainee busy; add to this flight
                Training_jump training_jump = new Training_jump(training_jump_id, earliest_training_flight, trainee, (Instructor)instructor);
                earliest_training_flight.addTraining_jump(training_jump);
                trainee.addBusytime(earliest_training_flight.getStarttime(), earliest_training_flight.getEndtime().plusMinutes(DEBRIEFING_TIME));
                instructor.addBusytime(earliest_training_flight.getStarttime(), earliest_training_flight.getEndtime().plusMinutes(DEBRIEFING_TIME+REPACK_TIME));
                print_success(earliest_training_flight.getId(),earliest_training_flight.getDropzone(),isprint);
                break;

            case "fun":
                String fun_jump_id = json.getString("id");
                LocalDateTime fun_jump_starttime = LocalDateTime.parse(json.getString("starttime"));
                JSONArray licenced_jumpers = json.getJSONArray("skydivers");

                //variable used for calcu
                List<Flight> fun_jump_flights = new ArrayList<Flight>();
                List<Student> fun_jump_skydivers = new ArrayList<Student>();
                List<Flight> fun_jump_satisfied_flights = new ArrayList<Flight>();
                Flight earliest_fun_jump_flight = null; //the right flight
                LocalDateTime earliest_fun_jumpstarttime = null;
                LocalDateTime earliest_fun_jumpendtime = null;


                // find a available flight
                fun_jump_flights = find_available_flights(fun_jump_starttime,licenced_jumpers.length());


                //find which flight can allow passenger and tandem to go on that day
                for (Flight fun_jump_flight : fun_jump_flights) {
                    int fun_jump_flag = 1;
                    earliest_fun_jumpstarttime = fun_jump_flight.getStarttime();
                    earliest_fun_jumpendtime = fun_jump_flight.getEndtime();



                    // check all fun_jump_skydivers are licenced_jumpers and free
                    for(int i = 0; i < licenced_jumpers.length(); i++) {
                        if (findStudent(licenced_jumpers.getString(i)) instanceof Licenced_jumper 
                        && check_student(findStudent(licenced_jumpers.getString(i)), earliest_fun_jumpstarttime, earliest_fun_jumpendtime.plusMinutes(REPACK_TIME))){
                            //add to fun_jump_skydivers if have't see brfore
                            if (!fun_jump_skydivers.contains(findStudent(licenced_jumpers.getString(i))) ){
                                fun_jump_skydivers.add(findStudent(licenced_jumpers.getString(i)));
                            }
                        } else {
                            fun_jump_flag = 0; //indicate find a student not licenced_jumpers or busy
                            break;
                        }
                    }
                    if (fun_jump_flag == 1){
                        fun_jump_satisfied_flights.add(fun_jump_flight);
                    }

                }

                //
                // find most appropriate flight
                earliest_fun_jump_flight = find_most_appropriate_flight(find_earliest_flights(fun_jump_satisfied_flights));
                if (earliest_fun_jump_flight == null){
                    print_rejected(isprint);
                    break;
                }

                // create fun_jump if vaild ; mark licenced_jumpers busy; add to this flight
                Fun_jump fun_jump = new Fun_jump(fun_jump_id, earliest_fun_jump_flight, fun_jump_skydivers);
                earliest_fun_jump_flight.addFun_jump(fun_jump);
                for(int i = 0; i < licenced_jumpers.length(); i++) {
                    findStudent(licenced_jumpers.getString(i)).addBusytime(earliest_fun_jump_flight.getStarttime(), earliest_fun_jump_flight.getEndtime().plusMinutes(REPACK_TIME));
                }
                print_success(earliest_fun_jump_flight.getId(),earliest_fun_jump_flight.getDropzone(),isprint);
                break;
            }
            break;
        case "change":
            String change_id = json.getString("id");
            JSONObject saved_command = null;
            //find pervious jump and save command
            saved_command = findCommands(change_id);

            //if no jump before
            if (saved_command == null){
                print_rejected(true);
                break;
            }

            //remove pervious jump
            JSONObject remove_command = new JSONObject();
            remove_command.put("command", "cancel");
            remove_command.put("id", saved_command.getString("id"));
            processCommand(remove_command, false);

            //check if change is vaild and save the result
            JSONObject change_command = json;
            change_command.remove("command");
            change_command.put("command", "request");
            processCommand(change_command, false);
            JSONObject saved_result = result;

            //if not recover pervious jump
            if (saved_result.getString("status").equals("rejected")){
                processCommand(saved_command, false);
                print_rejected(true);
                break;
            }

            //if vaild print result
            if (saved_result.getString("status").equals("success")){
                System.out.println(saved_result.toString(2));
            }
            break;
        case "cancel":
            String cancel_id = json.getString("id");
            //find exsit jump by id
            //remove jump from flight
            //remove busytime
            //remove command
            commands.remove(findCommands(cancel_id));
            for(int i = 0; i < flights.size(); i++) {
                flights.get(i).cancel(cancel_id);
            }
            break;
        case "jump-run":
            String flight_id = json.getString("id");
            JSONArray jump_run = new JSONArray();
            Flight jump_run_flight = null;
            flights.sort(new flightsorter());

            //find flight with given id
            for (Flight each_flight : flights) {
                if (each_flight.getId().equals(flight_id)){
                    jump_run_flight = each_flight;
                }
            }

            //find all skydivers
            if (!jump_run_flight.fun_jump_run().isEmpty()){
                for (JSONObject tmp : jump_run_flight.fun_jump_run()) {
                    jump_run.put(tmp);
                }
            }
            if (!jump_run_flight.training_run().isEmpty()){
                for (JSONObject tmp : jump_run_flight.training_run()) {
                    jump_run.put(tmp);
                }
            }
            if (!jump_run_flight.tandem_run().isEmpty()){
                for (JSONObject tmp : jump_run_flight.tandem_run()) {
                    jump_run.put(tmp);
                }
            }

            System.out.println(jump_run.toString(2));
            break;
        }
    }

 

    public static void main(String[] args) {
        SkydiveBookingSystem system = new SkydiveBookingSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command,true);
            }
        }
        sc.close();
    }

}
