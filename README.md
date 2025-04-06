# Linguistic Phenomena Tools

This is the repository of the [Linguistic Phenomena Ontology (LiPh)](https://gallosiciliani.unict.it/ns/liph),
an [Ontolex-lemon](https://www.w3.org/2016/05/ontolex/) extension devoted to represent linguistic phenomena. 

We denote as *linguistic phenomena* those kinds of phenomena which cause modifications 
of language expressions. In addition, we say that such a linguistic phenomenon is 
a *feature* of a specific language if it occurred in the generation of a relevant amount 
of elements of this language.

In addition, the repository contains a LiPh based [ontology 
of features of Gallo-Sicilian varieties](https://gallosiciliani.unict.it/ns/gs-features), i.e. those Gallo-Italic 
varieties spoken in Sicily. 

And, in <code>/examples/nicosia</code>, some example derivations of lexical expression
of the Gallo-Sicilian variety of Nicosia from their Latin etymons.

Finally, a validator to check whether the derivations in a dataset are compliant
with the linguistic phenomena definitions provided in their owns ontology is available as
Java ARchive <code>liph-1.0.2-jar-with-dependencies.jar</code>.

## Build

Build the whole tools from sources using maven as usual, i.e., by running the following command in the project root

    mvn clean install

The ontologies `liph` and `gs-features` are provided as owl files in the resources folder of the respective projects. 

Instead, the dataset containing nouns and verbs of the vocabulary of Nicosia e Sperlinga has to be generated from the PDF version
of [Vocabolario del dialetto galloitalico di Nicosia e Sperlinga](https://www.csfls.it/res/edizioni/catalogo/als/materiali-ricerche-dellals/alsm39-salvatore-trovato-salvatore-menza-vocabolario-dialetto-galloitalico-nicosia-sperlinga/).
Provided that you have this vocabulary in PDF format, the corresponding ttl file have to be
generated using the import utils provided in the `importing` project, with the following command

    importing-1.12.0-jar-with-dependencies.jar <pdfvocabularyfilpath>

It will be produce a turtle file named `nicosiaesperlinga.ttl`. This
file has to be placed in the execution directory of the web application.
However, this file, produced with the import tool as described before, is made
available in the project root.