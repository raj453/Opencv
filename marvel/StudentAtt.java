package cultoftheunicorn.marvel;

public class StudentAtt {
        private String dttime;
        private String roll;
        private String clas;
        private String status;
       private String subj;


    public StudentAtt() {
        }
        public StudentAtt(String dttime, String roll, String clas,String subj,String status) {
            this.dttime = dttime;
            this.roll = roll;
            this.clas = clas;
            this.status = status;
            this.subj = subj;
    }

        public String getDt() {
        return dttime;
    }
        public void setDt(String dttime) {
        this.dttime = dttime;
    }

        public String getRoll() {
            return roll;
        }
        public void setRoll(String roll) {
            this.roll = roll;
        }

        public String getClas() {
            return clas;
        }
        public void setClas(String clas) {
            this.clas = clas;
        }

        public String getStatus() {
        return status;
    }
        public void setStatus(String status) {
        this.status = status;
    }

        public String getSubj() {
        return subj;
    }
        public void setSubj(String subj) {
        this.subj = subj;
    }

        @Override
        public String toString() {
            return "";
        }
    }