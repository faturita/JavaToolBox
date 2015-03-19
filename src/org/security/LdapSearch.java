package org.security;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * Please document me !
 * 
 * User: vexrarod Date: 14/07/2004 Time: 18:18:05
 * 
 */

public class LdapSearch {
    public static void main(String[] args) {
        Hashtable env = new Hashtable();

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        String ldapUrl = "ldap://lnx-sistemas:389/dc=visa,dc=com,c=AR";
        env.put(Context.PROVIDER_URL, ldapUrl);

        try {
            DirContext dctx = new InitialDirContext(env);

            String base = "ou=People";

            SearchControls sc = new SearchControls();
            String[] attributeFilter = { "cn" };
            sc.setReturningAttributes(attributeFilter);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            // String filter = "(&(sn=W*)(l=Cup*))";
            String filter = "(sn=*)";

            NamingEnumeration results = dctx.search(base, filter, sc);
            System.out.println("Employees:");

            while (results.hasMore()) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();

                Attribute attr = attrs.get("cn");
                System.out.println(attr.get() + ":");
                attr = attrs.get("cn");
                System.out.println(attr.get());
            }

            dctx.close();
            System.out.println("Goodbyt!");

        } catch (NamingException ne) {
            ne.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
