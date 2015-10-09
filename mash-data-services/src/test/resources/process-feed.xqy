xquery version "1.0-ml";
import module namespace ing = "http://marklogic.com/solution/ns/mash/lib/ingest/ingest-lib" at "/app/lib/ingest/ingest-lib.xqy";

let $uri := "/raw/sp/GUARDIAN/GUARDIAN.xml"
let $agency     := tokenize($uri,'/')[3]
let $source     := tokenize($uri,'/')[4]

return ing:process($agency, $source, $uri)
