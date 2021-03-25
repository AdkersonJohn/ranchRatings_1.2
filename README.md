# ranchRatings_1.1
Refactored app to the Fragment View Model template for increased effectiveness.
# ranchRatings_1.1
Fixing unrelated histories issue
# ranchRatings_1.0
fixed corrupt gradle issues

# Ranch Ratings

---

Design Document

Jamie Miozzi

Alexander Burnett

John Adkerson

## Introduction

Ever wanted to find which restaurants have the best house-made ranch dressing? Ranch Ratings is here for you:

- Find the geographical location of the eating establishment you wish to rate
- Post ratings of 1 to 5 stars as well as comments pertaining to a restaurant’s "ranch" performance
- Track which establishments are receiving the highest ratings in your area
- Develop a profile and meet other ranch afficionados

Use your android device to create your own profile.  Use the search function, or geo-locater to find the establishment you wish to rate.  Scroll through the "news feed" to determine what restaurants are performing the best.  Log user history and ratings to develop a reputation. 

## Storyboard

![readmesnapShot](https://user-images.githubusercontent.com/61293447/109848321-1cdc6100-7c1e-11eb-90e9-f690e51942ed.PNG)
![readMeSnapShot01](https://user-images.githubusercontent.com/61293447/109848473-501ef000-7c1e-11eb-8aaf-181646ada946.PNG)

## Functional Requirements

### Requirement 1.0: Search for Establishments

#### Scenario

As a user interested in ranch, I want to be able to search for specific places based upon the name of the establishment

#### Dependencies

Restaurant data is available and accessible

#### Assumptions

Geographic location is revealed on map

Information about the establishment will be listed below the map screen

#### Examples
1.1

**Given** a feed a restaurant data is available

**When**  I search for "Texas Roadhouse"

**Then** I should receive at least one result of a nearby location

1.2

**Given** a feed a restaurant data is available

**When**  I search for "Mitas"

**Then** I should receive at least one result of a nearby location


### Requirement 2.0: Post Ratings to Establishment's Page

#### Scenario

As a user interested in ranch, I want to be able to post a rating on an establishments page for public viewing

#### Dependencies
establishment data are available and accessible.  
The device has GPS capabilities, and the user has granted location access. 

#### Assumptions
If a review is posted, the user should be able to return to the establishments page and see their review.

#### Examples

2.1

**Given** a feed a restaurant data is available

**Given** GPS details are available 

**When** 

- Select the establishment "Texas Roadhouse"
- Add pleasurable notes and 5-star review, then press "submit"

**Then** The good review is posted public for all users to view on Texas Roadhouse's landing page.

2.2

**Given** a feed a restaurant data is available

**Given** GPS details are available 

**When**

- Select the establishment "Mitas"
- Add poor notes and 1 star review, then press "submit"

**Then** The bad review is posted public for all users to view on Mitas' landing page.

## Class Diagram

![UML (1)](https://user-images.githubusercontent.com/61293447/109848641-7e9ccb00-7c1e-11eb-80de-0a4087fe2f30.png)

### Class Diagram Description


**Map:**  The first screen the user sees.  This will have geographical map, and search bar via which user can search for establishments.  

**Marker:**  Enables users to set markers on desired locations.  

**InfoWindow:** When a user reaches a location they desire, they will click on the location and a window displaying the establishments landing page will appear.  

**RanchDatabase:** Stores all user/establishment data.  


## Scrum Roles

- DevOps/Product Owner/Scrum Master: John Adkerson  
- Frontend Developer:  Alexander Burnett
- Integration Developer: Jamie Miozzi

## Weekly Meeting

Wednesday at 4pm via Microsoft Teams:

Contact personal email for a personal invitation
adkersjv@mail.uc.edu
