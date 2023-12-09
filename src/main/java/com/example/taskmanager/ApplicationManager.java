package com.example.taskmanager;

public class ApplicationManager {
        private static ApplicationManager instance;

        private ApplicationManager() {}

        public static ApplicationManager getInstance() {
            if (instance == null) {
                instance = new ApplicationManager();
            }
            return instance;
        }
}

