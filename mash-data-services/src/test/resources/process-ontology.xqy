import module namespace sem = "http://marklogic.com/semantics" at "/MarkLogic/semantics.xqy";

for $ontology in cts:uri-match("/ontology/*")
return sem:rdf-insert( sem:rdf-parse( doc($ontology), "turtle" ), (), (), ('ontology'))