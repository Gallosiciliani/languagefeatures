package it.unict.gallosiciliani.importing.csv;

import cz.cvut.kbss.jopa.model.MultilingualString;
import it.unict.gallosiciliani.liph.model.lemon.ontolex.Form;
import lombok.Getter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Provide latin forms using a cache to avoid replicate forms.
 * Insert new forms in the knowledge base if required.
 */
public class CachingLatinFormProvider implements LatinFormProvider {

    private final Map<String, URI> externalLinks = new TreeMap<>();


    /**
     * Create an empty {@link CachingLatinFormProvider} which will provide links to
     * Word Formation Latin lexicon in lila-erc.eu
     */
    public CachingLatinFormProvider() {
        //TODO extract to linked latin finder
        final String[] headers = {"entry","lemma","writtenRep"};
        final CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setSkipHeaderRecord(true).build();

        //import Word Formation Latin - derivational lexicon for latin
        //see https://lila-erc.eu/data/lexicalResources/WFL/Lexicon
        try(final InputStream wflInputStream = ClassLoader.getSystemResourceAsStream("WFL.csv");
            final InputStreamReader wflReader = new InputStreamReader(Objects.requireNonNull(wflInputStream))){
            format.parse(wflReader).stream().forEach(strings -> {
                final String writtenRep = strings.get("writtenRep");
                final URI link = URI.create(strings.get("lemma"));
                externalLinks.put(writtenRep, link);
            });
        } catch (final IOException e){
            throw new RuntimeException("Unable to read WFL.csv from resources");
        }
    }
    /**
     * Cache associating written representations to the corresponding forms in the
     * knowledge base
     */
    private final Map<String, Form> cache = new TreeMap<>();

    /**
     * Number of different forms which has been requested using getLatinForm
     */
    @Getter
    private int requestedForms;

    /**
     * Number of different forms which has been added to the knowledge base
     */
    @Getter
    private int createdForms;

    @Getter
    private int formsWithExternalLinks;

    @Override
    public Form getLatinForm(final String latinExpression, final String suggestedIRI) {
        final String writtenRep = normalize(latinExpression);
        final Form cachedForm = cache.get(writtenRep);
        if (cachedForm!=null)
            return cachedForm;
        requestedForms++;
        final Form novelForm = getNovelForm(writtenRep, latinExpression, suggestedIRI);
        novelForm.setId(suggestedIRI);
        cache.put(writtenRep, novelForm);
        return novelForm;
    }

    @Override
    public Stream<String> getAll() {
        return externalLinks.keySet().stream();
    }

    /**
     * Produce a normalized representation of a phonetic latin string which can be used as
     * written represenation.
     * @param phoneticRep a string which may contain phonetic chars
     * @return a string without phonetic chars
     */
    public static String normalize(final String phoneticRep) {
        return StringUtils.stripAccents(phoneticRep).toLowerCase().replaceAll("-", "");
    }

    /**
     * Get the {@link Form} individual with the given written representation, if any.
     * Create a new one otherwise.
     *
     * @param writtenRep      String written representation
     * @param label string to be used as label
     * @return a {@link Form} with the specified written representation
     */
    private Form getNovelForm(final String writtenRep, final String label, final String iri){
        final Form f = new Form();
        f.setId(iri);
        f.setWrittenRep(new MultilingualString().set(writtenRep));
        f.setLabel(label);

        final URI seeAlso = externalLinks.get(writtenRep);
        if (seeAlso!=null){
            f.setSeeAlso(seeAlso);
            formsWithExternalLinks++;
        }
        createdForms++;
        return f;
    }
}
