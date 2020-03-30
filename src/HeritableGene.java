public abstract class HeritableGene extends Gene
{
    protected int getInheritanceIndex(int[] geneCounts) {
        int result = -1;

        //first check for a majority (>50%)
        int total = 0;
        for(int i: geneCounts) {
            total += i;
        }//end for i

        if(total > 0) {
            for(int i = 0; i < geneCounts.length; i++) {
                if(geneCounts[i] / total > 0.50) {
                    result = i;
                    break;
                }//end if
            }//end for i
        }//end if

        //otherwise use the first index with count > 0
        if( result < 0) {
            for(int i = 0; i < geneCounts.length; i++) {
                if(geneCounts[i] > 0) {
                    result = i;
                    break;
                }//end if
            }//end for i
        }//end if

        return result;
    }
}
