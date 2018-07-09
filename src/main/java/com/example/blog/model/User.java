package com.example.blog.model;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;

@Entity
public class User {

    public static final int SALT_LENGTH = 32;
    public static final int KEY_SIZE = 128;
    public static final int ITERATIONS = 16;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String passwordHash;

    private String passwordSalt;

    private String emailAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public User() {

    }

    public User(String username, String password, String emailAddress) {
        byte[] salt = new byte[SALT_LENGTH];
        (new SecureRandom()).nextBytes(salt);
        this.username = username;
        this.passwordHash = User.hashPassword(password, salt);
        this.passwordSalt = Base64.getEncoder().encodeToString(salt);
        this.emailAddress = emailAddress;
        created = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public static String hashPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKeyFactory secretKeyFactory = null;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = null;
        try {
            hash = secretKeyFactory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean auth(String password) {
        byte[] mySalt = Base64.getDecoder().decode(passwordSalt);
        String hash = User.hashPassword(password, mySalt);
        return hash.equals(passwordHash);
    }
}
