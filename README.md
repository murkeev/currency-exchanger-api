<h1 align="center">Currency Exchange RESTful API </h1>

<h2>Project Overview</h2>
<p> This project is a RESTful API for a currency exchange application. The API provides functionality for user authentication (login and registration with JWT token security), managing user information, and tracking user currency conversion history. </p>

<h2>Features</h2>
<ul> <li><strong>User Authentication</strong>: Secure login and registration using JWT tokens.</li> <li><strong>Currency Conversion History</strong>: Track user conversion operations.</li> <li><strong>User Management</strong>: CRUD operations for managing user data.</li> </ul>

<h2>Technologies Used</h2>
<ul> <li><strong>Security</strong>: Spring Security with JWT</li> <li><strong>Database</strong>: PostgreSQL</li> <li><strong>ORM</strong>: Hibernate</li> <li><strong>Caching</strong>: Redis</li> <li><strong>REST APIs</strong>: Spring Web</li> <li><strong>Data Persistence</strong>: Spring Data JPA</li> </ul>

<h2>Prerequisites</h2>
<p>To run the project, you need the following:</p> <ul> <li>Java 17+</li> <li>PostgreSQL (or another SQL database of your choice)</li> <li>Redis</li> </ul>

<h2>Installation and Setup</h2>
<ol> <li><strong>Clone the repository</strong>: <pre><code>git clone https://github.com/murkeev/currency-exchanger-api.git</code></pre> </li>
<li><strong>Update credentials</strong>: Open the <code>application.properties</code> file and insert your database and Redis credentials:
  <pre><code>spring.datasource.driver-class-name=${DB_DRIVER_CLASS_NAME}
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=${HIBERNATE_DDL_AUTO}
spring.cache.type=${CACHE_TYPE}
spring.data.redis.host=${REDIS_HOST}
spring.data.redis.port=${REDIS_PORT}
spring.data.redis.database=${REDIS_DATABASE}
</code></pre> </li>
<li><strong>Run the project</strong>: Use your preferred method (e.g., IDE or terminal) to build and run the application.</li>
</ol>
<h2>API Endpoints</h2>
<ul> <li><strong>User Registration</strong>: <code>/api/register</code> - POST</li> <li><strong>User Login</strong>: <code>/api/login</code> - POST</li> <li><strong>Get User History</strong>: <code>/api/history</code> - GET (requires JWT token)</li> </ul>
<h2>Example Usage</h2>
<p>To register a new user, send a <code>POST</code> request to <code>/api/register</code> with the following body:</p> <pre><code>{ 
  "username": "yourUsername",
  "password": "yourPassword",
  "email": "yourEmail@example.com" 
} </code></pre> <p>For logging in, use the <code>/api/login</code> endpoint to obtain a JWT token, which can be used for authorized requests such as accessing the user history.</p>
