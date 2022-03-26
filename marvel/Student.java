package cultoftheunicorn.marvel;

public class Student {
        private String roll;
        private String name;
        private String clas;
        private String mob;
        private String photo;

        public Student() {
        }

        public Student(String roll,String name,String clas, String mob,String photo) {
            this.roll = roll;
            this.name = name;
            this.clas = clas;
            this.mob = mob;
            this.photo = photo;
        }
        public String getRoll() {
            return roll;
        }
        public void setRoll(String roll) {
            this.roll = roll;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getClas() {
            return clas;
        }
        public void setClas(String clas) {
            this.clas = clas;
        }

        public String getMob() {
        return mob;
    }
        public void setMob(String mob) {
        this.mob = mob;
    }

        public String getPhoto() {
            return photo;
        }
        public void setPhoto(String photo) {
            this.photo = photo;
        }

        @Override
        public String toString() {
            return "";
        }

    }