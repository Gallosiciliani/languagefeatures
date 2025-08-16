# Tool-Chain
##GalloSicilian derivations extractor

`java -jar gs-derivationsextractor inputTTLFile outputCSVFile`

Extract all the derivations described in the turtle file `inputTTLFile`, provided that the linguistic phenomenon appearing there
are in the _GalloSicilian Features Ontology_, and produce a corresponding CSV file `outputCSVFile` with the
following columns:

- **id** a unique identifier for the row;
- **lemma vnisp**, indicating the Gallo-Sicilian lemma ending the derivation;
- **derivazione** containing the derivation;
- **tratti disattesi**, enumerating all the Gallo-Sicilian features which could have affected the etymon, but are not in the derivation;
- **nuovo indice di galloitalicità**, the rate between the total number of the Gallo-Sicilian features which could have 
affected the etymon and those that occurred in the derivation;
- **Afer** `sì` if there is some feature belonging to the category [Apheresis](https://gallosiciliani.unict.it/ns/gs-features#Afer), `no` otherwise;
- **Assib** `sì` if there is some feature belonging to the category [Assibilation](https://gallosiciliani.unict.it/ns/gs-features#Assib), `no` otherwise;
- **Degem** `sì` if there is some feature belonging to the category [Degemination](https://gallosiciliani.unict.it/ns/gs-features#Degem), `no` otherwise;
- **Deretr** `sì` if there is some feature belonging to the category [Deretroflexion](https://gallosiciliani.unict.it/ns/gs-features#Deretr), `no` otherwise;
- **Dissim** `sì` if there is some feature belonging to the category [Dissimilation](https://gallosiciliani.unict.it/ns/gs-features#Dissim), `no` otherwise;
- **Ditt** `sì` if there is some feature belonging to the category [Diphthongization](https://gallosiciliani.unict.it/ns/gs-features#Ditt), `no` otherwise;
- **Leniz** `sì` if there is some feature belonging to the category [Lenition](https://gallosiciliani.unict.it/ns/gs-features#Leniz), `no` otherwise;
- **Palat** `sì` if there is some feature belonging to the category [Palatalization](https://gallosiciliani.unict.it/ns/gs-features#Palat), `no` otherwise;
- **Vocal** `sì` if there is some feature belonging to the category [Vocalization](https://gallosiciliani.unict.it/ns/gs-features#Vocal), `no` otherwise;
- **microtratto 1** for the first feature in the derivation, if any;
- **microtratto 2** for the second feature in the derivation, if any;
- **microtratto 3** for the third feature in the derivation, if any;
- **microtratto 4** for the fourth feature in the derivation, if any;
- **microtratto 5** for the fifth feature in the derivation, if any;
- **microtratto 6** for the sixth feature in the derivation, if any;
- **microtratto 7** for the seventh features in the derivation, if any;
- **microtratto 8** for the eighth features in the derivation, if any.
