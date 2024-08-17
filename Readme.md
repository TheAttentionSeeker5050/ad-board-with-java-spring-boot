# Kaijoo, a Spring Boot API Kijiji Clone
Kaijoo is an ad board where users can post or browse among different classified ads categories. Made on the following technologies: Spring Boot, JBDC and MySQL.


## Data models
- **Post:** Each individual post that users can create.
- **Category:** Categories of post
- **Tag:** Tags associated to posts
- **User:** The users, they can either be admin or regular users, they can buy or sell goods and services.
- **MediaItem:** A media item url associated to a post, these can be videos, images, recordings, etc.
- **SubCategory:** SubCategories of posts
- **SocialLink:** Social links associated to a post
- **Convesation:** This hold the id of the conversations as well as the id of the users that are part of the conversation. This will play a role when integrating to a messaging service such as firebase.

# Features
Features are being implemented at the moment, the following is a list of features will be present in the final version:
- User can create an account
- User can login
- User can create a post
- User and anonymous user can view all posts
- User and anonymous user can view a single post
- User can update a post
- User can delete a post
- Advanced search option with semantic search capabilities
- Admin user can add or modify tags, categories, subcategories
- Post can have multiple media items and social media links
- This API will be integrated with a messaging service such as firebase to allow users to communicate with each other.
- This API will be integrated with a front end application that will consume the API.

