package com.android.pyp.property;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by devel-73 on 19/8/17.
 */

public class PropertyData implements Parcelable {

    private String propertyId;
    private String ownerId;
    private String nationality;
    private String gender;
    private String price;
    private String bhk;
    private String sqft;
    private String currency;
    private String address;
    private String landmark;
    private String description;
    private String contactNum;
    private String amenties;
    private String city;
    private String state;
    private String country;
    private double latitude;
    private double longitude;
    private String adminVerify;
    private String imageName;
    private String propertyType;
    private List<PropertyData> amentiesList;
    private List<PropertyData> rulesList;
    private String amentyName;
    private String amentyImg;
    private String rulesName;
    private String rulesImg;
    private String shareLink;
    private String title;
    private String fId;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public PropertyData() {
    }

    protected PropertyData(Parcel in) {
        propertyId = in.readString();
        ownerId = in.readString();
        nationality = in.readString();
        gender = in.readString();
        price = in.readString();
        bhk = in.readString();
        sqft = in.readString();
        currency = in.readString();
        address = in.readString();
        landmark = in.readString();
        description = in.readString();
        contactNum = in.readString();
        amenties = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        adminVerify = in.readString();
        imageName = in.readString();
        propertyType = in.readString();
        amentiesList = in.createTypedArrayList(PropertyData.CREATOR);
        rulesList = in.createTypedArrayList(PropertyData.CREATOR);
        amentyName = in.readString();
        amentyImg = in.readString();
        rulesName = in.readString();
        rulesImg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(propertyId);
        dest.writeString(ownerId);
        dest.writeString(nationality);
        dest.writeString(gender);
        dest.writeString(price);
        dest.writeString(bhk);
        dest.writeString(sqft);
        dest.writeString(currency);
        dest.writeString(address);
        dest.writeString(landmark);
        dest.writeString(description);
        dest.writeString(contactNum);
        dest.writeString(amenties);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(adminVerify);
        dest.writeString(imageName);
        dest.writeString(propertyType);
        dest.writeTypedList(amentiesList);
        dest.writeTypedList(rulesList);
        dest.writeString(amentyName);
        dest.writeString(amentyImg);
        dest.writeString(rulesName);
        dest.writeString(rulesImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PropertyData> CREATOR = new Creator<PropertyData>() {
        @Override
        public PropertyData createFromParcel(Parcel in) {
            return new PropertyData(in);
        }

        @Override
        public PropertyData[] newArray(int size) {
            return new PropertyData[size];
        }
    };

    public String getAmentyName() {
        return amentyName;
    }

    public void setAmentyName(String amentyName) {
        this.amentyName = amentyName;
    }

    public String getAmentyImg() {
        return amentyImg;
    }

    public void setAmentyImg(String amentyImg) {
        this.amentyImg = amentyImg;
    }

    public String getRulesName() {
        return rulesName;
    }

    public void setRulesName(String rulesName) {
        this.rulesName = rulesName;
    }

    public String getRulesImg() {
        return rulesImg;
    }

    public void setRulesImg(String rulesImg) {
        this.rulesImg = rulesImg;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public List<PropertyData> getAmentiesList() {
        return amentiesList;
    }

    public void setAmentiesList(List<PropertyData> amentiesList) {
        this.amentiesList = amentiesList;
    }

    public List<PropertyData> getRulesList() {
        return rulesList;
    }

    public void setRulesList(List<PropertyData> rulesList) {
        this.rulesList = rulesList;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBhk() {
        return bhk;
    }

    public void setBhk(String bhk) {
        this.bhk = bhk;
    }

    public String getSqft() {
        return sqft;
    }

    public void setSqft(String sqft) {
        this.sqft = sqft;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getAmenties() {
        return amenties;
    }

    public void setAmenties(String amenties) {
        this.amenties = amenties;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdminVerify() {
        return adminVerify;
    }

    public void setAdminVerify(String adminVerify) {
        this.adminVerify = adminVerify;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
