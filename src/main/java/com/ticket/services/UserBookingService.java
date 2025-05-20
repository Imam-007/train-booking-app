package com.ticket.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.entities.Train;
import com.ticket.entities.Users;
import com.ticket.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private Users user;

    private List<Users> usersList;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "src/main/java/com/ticket/localDB/users.json";


    public UserBookingService(Users user) throws IOException {
        this.user = user;
        loadUser();
    }

    public UserBookingService() throws IOException {
        loadUser();
    }

    public List<Users> loadUser() throws IOException {
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<Users>>() {});
    }

    public Boolean loginUser(){
        Optional<Users> foundUser = usersList.stream().filter(user1 -> user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword())).findFirst();

        return foundUser.isPresent();
    }

    public Boolean signUp(Users user) {
        try {
            usersList.add(user);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, usersList);
    }

    public void featchBookings() {
        user.printTickets();
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }
}
