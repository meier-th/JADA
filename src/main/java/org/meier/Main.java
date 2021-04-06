package org.meier;

import org.meier.loader.FSProjectLoader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            FSProjectLoader loader = new FSProjectLoader();
            loader.loadProject("/home/thom/IdeaProjects/communicator/src");
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
