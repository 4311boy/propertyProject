<h1>NTU-PACE SE9: Project 2</h1>

<h2>Brief</h2>
Create a spring boot rest api backend application connected to a PostgresQL database. Include the following:
<ol>
<li>Exception handling
    <ul>
    <li>Global exception handling</li>
    <li>Validation exceptions</li>
    <li>Custom and general exceptions</li>
    </ul>
</li>
<li>Validation</li>
<li>Logging of info and errors using slf4j and logback</li>
<li>Testing
    <ul>
    <li>Unit tests of service testing: write at least 3 tests</li>
    </ul>
</li>
</ol>
<br>
Frontend client is optional; Postman/ Thunderclient/ Swagger can be used to demonstrate the endpoints. 

<br>
Technical presentation guidelines:
<li>Problem statement</li>
<li>Tech stack</li>
<li>Screenshots of the working application</li>
<li>Learnings of each member</li>
<li>Challenges encountered during the project</li>
<li>Your contribution as an individual</li>


<h2>Outline</h2>

<h3>Entities</h3>

<li>User (Wee Pynn)</li>
<li>Agent (Chay Teng)</li>
<li>Listing (Luke)</li>

<h3>Endpoints</h3>

| Method        | Path                      | Function           |
| -----------   | -----------               | -----------        |
| POST          | /user                     | Create user        |
| GET           | /user/{id}                | Find user by Id    |
| PUT           | /user/{id}                | Update user by Id  |
| DELETE        | /user/{id}                | Delete user        |
| POST          | /agent                    | Create agent       |
| GET           | /agent/{id}               | Find agent by Id   |
| PUT           | /agent/{id}               | Update agent by Id |
| DELETE        | /agent/{id}               | Delete agent       |
| GET           | /listing/search           | Search listings based on parameters   |
| POST          | /listing/{agentId}        | Create listing       |
| PUT           | /listing/{agentId}/{id}   | Update listing by Id |
| DELETE        | /listing/{agentId}/{id}   | Delete listing       |

Note that only the agent that created (POST) the listing, can update (PUT) or delete (DELETE) it.

<h3>Logging Strategy</h3>
<li>Log only errors and warnings</li>
<li>Logs are saved in a separate file</li>
<li>Logs are appended</li>


<h2>Schedule</h2>

| Deliverable       | Deadline                      |
| -----------       | -----------                   | 
| Endpoints/ controller/ service/ entity/ repo layers             | Tuesday, 18 Mar|
| Logging and exceptions            | Thursday, 20 Mar|
| Test cases            | Saturday, 22 Mar|

Next physical meeting on Saturday, 22 Mar. Venue at Punggol Digital District. Time TBC.

<h2>Meeting Notes</h2>

<h3>18 Mar 25</h3>
<ul>
<li>Each do exception for own entity: Not Found Exception & Invalid Input Exception
<li>Each do logging for own entity
<li>Wee Pyn: User to save favourite listings: logic
<li>Luke: User authentication
<li>Luke: User authorisation
<li>Luke: SIT test case
</ul>


