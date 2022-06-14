package com.main.lab_3;

import java.io.IOException;

public interface extract_create {
    void file_extractor(String file_path) throws IOException;
    void file_writter() throws IOException;

    void create_student();
    void create_custom_student(String name, String year_of_birth, String attend);
}
