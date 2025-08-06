package model.MarketModel;


public class ChannelSummary {
    String channelName;
    int totalQuantity;
    int totalRevenue;
    int totalAdBudget;
    float profitPercent;
    int rank;

    public ChannelSummary(String channelName, int totalQuantity, int totalRevenue, int totalAdBudget) {
        this.channelName = channelName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
        this.totalAdBudget = totalAdBudget;
        this.profitPercent = (totalAdBudget > 0) ? ((float) (totalRevenue - totalAdBudget) / totalAdBudget) * 100 : 0;   //conditional statement, if totalAdBudget>0 else
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void printSummaryRow() {
        System.out.format("%-5d %-20s %-15d $%-15d $%-15d %-15.2f\n",
                rank, channelName, totalQuantity, totalRevenue, totalAdBudget, profitPercent);
    }

    public float getProfitPercentage() {
        return profitPercent;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalAdBudget() {
        return totalAdBudget;
    }
}
