for %%f in (C:\Users\eddox\Documents\GitHub\sparql-gremlin\test_queries\*) do mvn -q exec:java -e -Dexec.mainClass="com.datastax.sparql.SPARQL2GREMLIN" -Dexec.args="-t -g crew -f %%f"



