public class Event {
    private int time;
        private String type; // Event type as a String
        private Process process;

        public Event(int time, String type, Process process) {
            this.time = time;
            this.type = type;
            this.process = process;
        }

        public int getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public Process getProcess() {
            return process;
        }
    }