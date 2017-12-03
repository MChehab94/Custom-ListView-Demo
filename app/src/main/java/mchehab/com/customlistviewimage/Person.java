package mchehab.com.customlistviewimage;

import org.parceler.Parcel;

/**
 * Created by muhammadchehab on 10/29/17.
 */
@Parcel
public class Person {

    private String firstName;
    private String lastName;
    private String description;
    private String imageName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}