INSERT INTO AGENT (AGENTID,NAME, IMAGE) VALUES (1,'Clara', 'clara');
INSERT INTO AGENT (AGENTID,NAME, IMAGE) VALUES (2,'Mario', 'mario');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (1, 'SALUTATION', 'Phrase category for salutations of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (2, 'GOODBYES', 'Phrase category for goodbyes of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (3, 'MISUNDERSTANDINGS', 'Phrase category for misunderstandings of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (4, 'PHASE_SENTIMIENTO', 'Phrase category for first phase STOPP-SPA of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (5, 'PHASE_SUB_SENTIMIENTO', 'Phrase category for subsequende of second phase STOPP-SPA of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (6, 'PHASE_PROBLEMA', 'Phrase category for second phase STOPP-SPA of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (7, 'PHASE_SUB_PROBLEMA', 'Phrase category for subsequences for second phase STOPP-SPA of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (8, 'PHASE_OBJETIVO', 'Phrase category for third phase STOPP-SPA of the agents');
INSERT INTO PHRASECATEGORY (PHRASECATEGORYID, NAME, DESCRIPTION) VALUES (12, 'POSITIVE_FINISHING', 'Phrase category for ends the conversation in a good way');
INSERT INTO MOVEMENTTYPES (MOVEMENTTYPEID, NAME, DESCRIPTION) VALUES (1, 'LASTLOGIN', 'Last login of the user');
INSERT INTO MOVEMENTTYPES (MOVEMENTTYPEID, NAME, DESCRIPTION) VALUES (2, 'LASTCONVERSATION', 'Last conversation of the user with his agent');
INSERT INTO MOVEMENTTYPES (MOVEMENTTYPEID, NAME, DESCRIPTION) VALUES (3, 'DESERTION ', 'Desertion of the user without finishing the therapy');
INSERT INTO MOVEMENTTYPES (MOVEMENTTYPEID, NAME, DESCRIPTION) VALUES (4, 'COMPLETE ', 'User has completed the whole therapy');
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Hola %user%',1);
INSERT INTO AGENT_PHRASE VALUES (1,1,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,1,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Bienvenido %user%',1);
INSERT INTO AGENT_PHRASE VALUES (1,2,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,2,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Me alegro de verte, %user%',1);
INSERT INTO AGENT_PHRASE VALUES (1,3,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,3,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Qué tal, %user%?',1);
INSERT INTO AGENT_PHRASE VALUES (1,4,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,4,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Adiós %user%, espero volver a verte pronto',2);
INSERT INTO AGENT_PHRASE VALUES (1,5,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,5,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Hasta la próxima, %user%',2);
INSERT INTO AGENT_PHRASE VALUES (1,6,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,6,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Hasta luego, %user%',2);
INSERT INTO AGENT_PHRASE VALUES (1,7,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,7,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Perdona %user%, no te he entendido',3);
INSERT INTO AGENT_PHRASE VALUES (1,8,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,8,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Qué te pasa?',4);
INSERT INTO AGENT_PHRASE VALUES (1,9,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,9,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Qué te ocurre?',4);
INSERT INTO AGENT_PHRASE VALUES (1,10,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,10,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Cómo te sientes?',4);
INSERT INTO AGENT_PHRASE VALUES (1,11,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,11,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Lo que te ocurre es que estás %feeling%?',5);
INSERT INTO AGENT_PHRASE VALUES (1,12,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,12,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Estás %feeling%?',5);
INSERT INTO AGENT_PHRASE VALUES (1,13,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,13,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Ten encuentras %feeling%?',5);
INSERT INTO AGENT_PHRASE VALUES (1,14,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,14,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Te sientes %feeling%?',5);
INSERT INTO AGENT_PHRASE VALUES (1,15,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,15,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Por qué te sientes %feeling%?',6);
INSERT INTO AGENT_PHRASE VALUES (1,16,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,16,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Por qué estás %feeling%?',6);
INSERT INTO AGENT_PHRASE VALUES (1,17,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,17,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Cuál es la causa de que estés %feeling%?',6);
INSERT INTO AGENT_PHRASE VALUES (1,18,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,18,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('La causa de que estés %feeling% ¿es porque %problem%?',7);
INSERT INTO AGENT_PHRASE VALUES (1,19,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,19,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('¿Tú crees que estás %feeling% porque %problem%?',7);
INSERT INTO AGENT_PHRASE VALUES (1,20,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,20,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Mi objetivo es ayudarte a resolver que %problem%, ¿tú quieres resolver %problem% y encontrarte mejor',8);
INSERT INTO AGENT_PHRASE VALUES (1,21,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,21,CURRENT_TIMESTAMP);
INSERT INTO PHRASES (PHRASE, PHRASECATEGORYID) VALUES('Ya hemos terminado, si tienes alguna pregunta puedes hacérsela a tus padres o a tus profesores. ¡Que tengas muy buen día!',12);
INSERT INTO AGENT_PHRASE VALUES (1,22,CURRENT_TIMESTAMP);
INSERT INTO AGENT_PHRASE VALUES (2,22,CURRENT_TIMESTAMP);