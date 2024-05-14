# Football Ontology Creation and Quering

In this project we followed a series of steps:

## 1. Modeling the ontology using Protégé 
The ontology consists of classes which represent different types of entities within the football domain. These include:
- Championship: Represents football tournaments or leagues.
- Person: Represents individuals involved in football, such as players, coaches, and referees.
- Coach: Represents individuals who coach football teams. Coaches are subclassed from the more general class of Person.
- Match: Represents individual football matches between two teams.
- Player: Represents individuals who play football professionally or recreationally. Players are subclassed from the Person class.
- Referee: Represents individuals who officiate football matches. Referees are subclassed from the Person class.
- Team: Represents football teams or clubs.

Each class his its own data properties which capture attributes or characteristics of entities in the
ontology. These may include:
- Properties related to championship details such as name, country, type, and year.
- Properties related to match details such as date, location, title, and result.
- Properties related to person details such as name, age, gender, and nationality.
- Properties related to team details such as name and country.

There exist object properties that connect classes to each other:
- between_teams: Relates a match to the teams competing in it.
- coaches_in: Connects a coach to the team they coach.
- competes_in: Establishes the participation of a team in a match.
- contains_teams: Associates a championship with the teams participating in it.
- has_coach: Links a team to its coach.
- has_player: Connects a team to its players.
- has_referee: Indicates the referee officiating a match.
- participates_in: Establishes the participation of a team in a championship.
- plays_in: Connects a player to the team they play for.
- refereed: Indicates matches officiated by a referee.

Each class has specific restrictions and constraints:
- A coach must coach exactly one team.
- A match must involve exactly two teams and one referee.
- A player can play for at most one team.
- A team must have at least one coach, between 23 and 30 players, and compete in multiple matches.
- A team must participate in at least one championship.
- A referee must officiate at least one match.
- A match must have exactly one referee.

## 2. Populating the Ontology and Checking Consistency Using PELLET

We populated the ontology by adding many instances to each of the classes. After populating the ontology we checked for the consistency of the ontology using PELLET reasoner and there were no conflicts.

## 3. Quering the Ontology

We used the Apache Jena Fuseki endpoint to query our ontology using SPARQL.

## 4. Manipulating the Ontology Using Jena in Java.

We created 6 java files that queried and manipulated the ontology using Jena in Java language (using or without using) queries loaded from a text file and (using or without) inference.

## 5. Java UI Application 

The Java application implemented utilizes the OWLAPI and OpenPellet reasoner to provide a sophisticated interface for managing and viewing sports matches based on selected filters from all available teams and referees. The application is designed to enable users to dynamically include or exclude teams and referees to filter the view of sports matches. It uses a graphical user interface (GUI) to facilitate user interaction, allowing users to make selections.









****************************************************************************
This project is part of the Ontologies and Web Semantics course in the faculty of engineering, Ain Shams University under the supervision of Dr. Ensaf Hussein and Eng. Eman Khaled.
