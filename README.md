# ri-2017-trab4
Application and evaluation of methods for relevance feedback and thesaurus, based on query expansion.

## How To Run
Execute the application with the following command:
```
doc stopwords queries GS weights scores relevance
```
Parameters:
- **doc** - name of document/directory to read;
- **stopwords** - file of stopwords;
- **queries** - file of the queries;
- **GS** - file with the relevance of the queries (Gold Standard);
- **weights** - file to save the indexer with the associated weights;
- **scores** - file to save the scores;
- **relavance** - relevance's type. It can be explicit or implicit;

Example:
```
cranfield stopwords.txt cranfield.queries.txt cranfield.query.relevance.txt DocumentWeighter.txt ScoreResults.txt implicit
```
