package br.com.joinersa.contaeletrica;

/**
 * Created by Joiner on 16/07/2016.
 */
public enum Concessionaria {

    AES_SUL("AES-SUL", 0.48035f),
    AME("AmE", 0.44531f),
    AMPLA("AMPLA", 0.54195f),
    BANDEIRANTE("BANDEIRANTE", 0.50057f),
    BOA_VISTA("BOA VISTA", 0.40664f),
    CAIUA_D("CAIUA-D", 0.44736f),
    CEA("CEA", 0.30111f),
    CEAL("CEAL", 0.43911f),
    CEB_DIS("CEB-DIS", 0.43676f),
    CEEE_D("CEEE-D", 0.48317f),
    CELESC_DIS("CELESC-DIS", 0.44436f),
    CELG_D("CELG-D", 0.46660f),
    CELPA("CELPA", 0.52539f),
    CELPE("CELPE", 0.39524f),
    CEMAR("CEMAR", 0.46435f),
    CEMIG_D("CEMIG-D", 0.50974f),
    CEPISA("CEPISA", 0.43987f),
    CERON("CERON", 0.49235f),
    CFLO("CFLO", 0.55335f),
    CHESP("CHESP", 0.58191f),
    CNEE("CNEE", 0.42402f),
    COCEL("COCEL", 0.53199f),
    COELBA("COELBA", 0.38836f),
    COELCE("COELCE", 0.41796f),
    COOPERALIANCA("COOPERALIANCA", 0.55308f),
    COPEL_DIS("COPEL-DIS", 0.49231f),
    COSERN("COSERN", 0.37590f),
    CPFL_JAGUARI("CPFL-JAGUARI", 0.37656f),
    CPFL_LESTE_PAULISTA("CPFL-LESTE PAULISTA", 0.40695f),
    CPFL_MOCOCA("CPFL-MOCOCA", 0.45359f),
    CPFL_PAULISTA("CPFL-PAULISTA", 0.41964f),
    CPFL_PIRATININGA("CPFL-PIRATININGA", 0.51081f),
    CPFL_SANTA_CRUZ("CPFL-SANTA CRUZ", 0.45987f),
    CPFL_SUL_PAULISTA("CPFL-SUL PAULISTA", 0.41533f),
    DEMEI("DEMEI", 0.47132f),
    DMED("DMED", 0.49409f),
    EBO("EBO", 0.42868f),
    EDEVP("EDEVP", 0.45160f),
    EEB("EEB", 0.48437f),
    EFLIC("EFLIC", 0.52755f),
    EFLUL("EFLUL", 0.52263f),
    ELEKTRO("ELEKTRO", 0.51041f),
    ELETROACRE("ELETROACRE", 0.46327f),
    ELETROCAR("ELETROCAR", 0.52791f),
    ELETROPAULO("ELETROPAULO", 0.43611f),
    ELFSM("ELFSM", 0.51900f),
    EMG("EMG", 0.50110f),
    EMT("EMT", 0.46520f),
    ENERSUL("ENERSUL", 0.46470f),
    ENF("ENF", 0.52054f),
    EPB("EPB", 0.41817f),
    ESCELSA("ESCELSA", 0.46452f),
    ESE("ESE", 0.40935f),
    ETO("ETO", 0.46203f),
    FORCEL("FORCEL", 0.55623f),
    HIDROPAN("HIDROPAN", 0.54650f),
    IENERGIA("IENERGIA", 0.45465f),
    LIGHT("LIGHT", 0.54346f),
    MUXENERGIA("MUXENERGIA", 0.48088f),
    RGE("RGE", 0.44680f),
    SULGIPE("SULGIPE", 0.51673f),
    UHENPAL("UHENPAL", 0.58908f);

    public String nome;
    public float valor;

    private Concessionaria(String nome, float valor) {
        this.valor = valor;
        this.nome = nome;
    }

    public static Concessionaria getEnumConcessionaria(int position) {
        return Concessionaria.values()[position];
    }


    public static String[] getArrayNomes() {
        String[] strConcessionarias = new String[62];
        int i = 0;
        for(Concessionaria con : Concessionaria.values()) {
            strConcessionarias[i++] = con.nome;
        }
        return strConcessionarias;
    }

}
