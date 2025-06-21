# JavaInHire Design Document

## 1. Problem Statement

JavaInHire is a platform designed to help junior and intermediate Java developers find job opportunities tailored to their experience level. The platform aggregates job postings from various sources (e.g., Indeed, LinkedIn, Glassdoor) and filters them to display only those relevant to Java developers with less than 4 years of experience. Users can view job postings, mark favorites, track their applications, and get redirected to the original job posting for further action.

The problem we aim to solve is the difficulty junior Java developers face in finding job postings that match their skill level and experience. By providing a curated list of job opportunities, JavaInHire simplifies the job search process and helps developers focus on relevant opportunities.

---

## 2. Top Questions to Resolve in Review

1. How can we efficiently fetch job postings from multiple external platforms (e.g., Indeed, LinkedIn) while ensuring data consistency and relevance?
2. What is the best way to handle pagination for job postings to ensure a smooth user experience?
3. How can we ensure the platform remains free to use while relying on free-tier AWS services and other free resources?
4. Should we implement a caching mechanism to reduce the load on external APIs and improve performance?
5. How can we handle user authentication securely without incurring additional costs?

---

## 3. Use Cases

U1. As a user, I want to view a list of job postings filtered for Java developers with less than 4 years of experience.

U2. As a user, I want to mark a job posting as a favorite so I can easily find it later.

U3. As a user, I want to track the status of my job applications (e.g., applied, interview scheduled, rejected).

U4. As a user, I want to click on a job posting to be redirected to the original source for further action.

U5. As a user, I want to view statistics about my job applications (e.g., number of applications, status breakdown).

U6. As a user, I want to create an account and log in to save my preferences and application history.

---

## 4. Project Scope

### 4.1. In Scope

- Fetching job postings from external platforms (e.g., Indeed, LinkedIn) and filtering them for Java developers with less than 4 years of experience.
- Displaying job postings with pagination.
- Allowing users to mark job postings as favorites.
- Tracking job application statuses (e.g., applied, interview, rejected).
- Redirecting users to the original job posting source.
- Providing a dashboard for users to view application statistics.
- User authentication and account management.

### 4.2. Out of Scope

- Integration with job boards that require paid API access.
- Advanced search filters beyond Java-specific roles and experience level.
- Email notifications for new job postings or application updates.
- Mobile app development.
- Integration with LinkedIn or other platforms for automatic application submission.

---

## 5. Proposed Architecture Overview

JavaInHire will be built using the following architecture:

1. **Frontend**: A static website built with HTML, CSS, and JavaScript. It will interact with the backend via API calls.
2. **Backend**: A Spring Boot application that handles business logic, interacts with the database, and serves API endpoints.
3. **Database**: PostgreSQL hosted on AWS RDS to store job postings, user data, and application statuses.
4. **Authentication**: AWS Cognito for user authentication and authorization.
5. **Job Fetching Service**: A scheduled AWS Lambda function that fetches job postings from external platforms and stores them in the database.
6. **Hosting**: The frontend will be hosted on GitHub Pages or Netlify, and the backend will be deployed on AWS Elastic Beanstalk.

This architecture ensures a clear separation of concerns, scalability, and cost-effectiveness by leveraging free-tier AWS services.

---

## 6. API

### 6.1. Public Models

```java
// JobPostingModel
String id;
String title;
String description;
String experienceLevel;
String source;
String link;
boolean isFavorite;
boolean isViewed;

// ApplicationModel
String id;
String userId;
String jobPostingId;
String status; // e.g., "Applied", "Interview", "Rejected"
LocalDateTime applicationDate;

// UserModel
String id;
String firstName;
String lastName;
String email;
String password;
```

### 6.2. Get Job Postings Endpoint

- **Endpoint**: `GET /job-postings`
- **Description**: Returns a paginated list of job postings filtered for Java developers with less than 4 years of experience.
- **Parameters**:
  - `page` (optional): Page number for pagination (default: 0).
  - `size` (optional): Number of job postings per page (default: 10).
- **Response**: A list of `JobPostingModel` objects.
- **Error Handling**:
  - If no job postings are found, returns an empty list.
  - If the database is unavailable, returns a `500 Internal Server Error`.

### 6.3. Mark Job as Favorite Endpoint

- **Endpoint**: `PUT /job-postings/{id}/favorite`
- **Description**: Marks a job posting as a favorite for the logged-in user.
- **Parameters**:
  - `id`: The ID of the job posting.
- **Response**: The updated `JobPostingModel` with `isFavorite` set to `true`.
- **Error Handling**:
  - If the job posting is not found, returns a `404 Not Found` error.
  - If the user is not authenticated, returns a `401 Unauthorized` error.

### 6.4. Track Application Status Endpoint

- **Endpoint**: `POST /applications`
- **Description**: Allows users to track the status of their job applications.
- **Request Body**: An `ApplicationModel` object.
- **Response**: The created `ApplicationModel` object.
- **Error Handling**:
  - If the job posting or user does not exist, returns a `400 Bad Request` error.
  - If the user is not authenticated, returns a `401 Unauthorized` error.

---

## 7. Tables

### 7.1. `job_postings`

```
id // primary key, UUID
title // string
description // text
experience_level // string
source // string
link // string
is_favorite // boolean
is_viewed // boolean
```

### 7.2. `applications`

```
id // primary key, UUID
user_id // foreign key, UUID
job_posting_id // foreign key, UUID
status // string
application_date // timestamp
```

### 7.3. `users`

```
id // primary key, UUID
first_name // string
last_name // string
email // string (unique)
password // string (hashed)
```

---

## 8. Pages

### Homepage
- Displays a list of job postings with pagination controls.
- Each job posting includes a title, description, experience level, and a "Mark as Favorite" button.
- A navigation bar allows users to log in or register.

![Homepage Mockup](images/homepage.png)

### Login Page
- Allows users to log in using their email and password.
- Includes a link to the registration page.

![Login Page Mockup](images/login.png)

### Registration Page
- Allows new users to create an account by providing their first name, last name, email, and password.

![Registration Page Mockup](images/register.png)

### Dashboard
- Displays statistics about the user's job applications (e.g., number of applications, status breakdown).
- Includes a list of favorited job postings.

![Dashboard Mockup](images/dashboard.png)

