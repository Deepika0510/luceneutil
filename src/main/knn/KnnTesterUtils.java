/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package knn;

import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

import static org.apache.lucene.search.DocIdSetIterator.NO_MORE_DOCS;

public class KnnTesterUtils {

  /** Fetches values for the "id" field from search results
   */
  public static int[] getResultIds(TopDocs topDocs, StoredFields storedFields) throws IOException {
    int[] resultIds = new int[topDocs.scoreDocs.length];
    int i = 0;
    // TODO: switch to doc values for this id field?  more efficent than stored fields
    // TODO: or, at least load the stored documents in index (Lucene docid) order to
    //       amortize cost of decompressing each stored doc block (hmm, though, this cost/time
    //       is not included in the reported benchy results... this is called after all KNN
    //       queries have run)
    for (ScoreDoc doc : topDocs.scoreDocs) {
      assert doc.doc != NO_MORE_DOCS: "illegal docid " + doc.doc + " returned from KNN search?";
      resultIds[i++] = Integer.parseInt(storedFields.document(doc.doc).get(KnnGraphTester.ID_FIELD));
    }
    return resultIds;
  }
}
