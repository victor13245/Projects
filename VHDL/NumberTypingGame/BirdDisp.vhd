library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity Disp is
    Port(JUMP : in std_logic_vector(3 downto 0);
         CLK : in std_logic;
         RST : in std_logic;
         EN : in std_logic;
         gameOverv : in std_logic;
         show : in std_logic;
         CS : out std_logic;
         SDO : out std_logic;
         SCLK : out std_logic;
         DC : out std_logic;
         FIN : out std_logic);
end Disp;

architecture Behavioral of Disp is
COMPONENT SPI_interface
    PORT(
        CLK : IN  std_logic;
        RST : IN  std_logic;
        SPI_EN : IN  std_logic;
        SPI_DATA : IN  std_logic_vector(7 downto 0);
        CS : OUT  std_logic;
        SDO : OUT  std_logic;
        SCLK : OUT  std_logic;
        SPI_FIN : OUT  std_logic
       );
END COMPONENT;

COMPONENT Delay
    PORT(
         CLK : IN  std_logic;
         RST : IN  std_logic;
         DELAY_MS : IN  std_logic_vector(11 downto 0);
         DELAY_EN : IN  std_logic;
         DELAY_FIN : OUT  std_logic
        );
END COMPONENT;

COMPONENT ascii_rom
  PORT (
    clk : IN STD_LOGIC;
    addr : IN STD_LOGIC_VECTOR(10 DOWNTO 0);
    dout : OUT STD_LOGIC_VECTOR(7 DOWNTO 0) 
  );
END COMPONENT;

type states is (Idle, ClearDC, SetPage, PageNum, LeftColumn1, LeftColumn2, SetDC, SendChar1, SendChar2, SendChar3, SendChar4, SendChar5, SendChar6, SendChar7, SendChar8, ReadMem, ReadMem2, Transition1, Transition2, Transition3, Transition4, Transition5, UpdateScreen, Command1, Command2, Command3, Command4, Command5, Command6, Command7, Command8, Command9, Command10, Command11, Command12, Command13, Command14, Command15, Command16, Command0, Wait1, Done, GameOver);
type OledMem is array(0 to 3, 0 to 15) of std_logic_vector(7 downto 0);
signal current_screen : OledMem;
constant ccommand1 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B0",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand2 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                 
constant ccommand3 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B2",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand4 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B3",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                 
constant ccommand5 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B4",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
 
 constant ccommand6 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B5",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                  
 constant ccommand7 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B6",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
 
 constant ccommand8 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B7",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));	

constant ccommand9 : OledMem := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B8",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand10 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B9",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                 
constant ccommand11 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B0",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand12 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B1",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand13 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B2",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand14 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B3",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                 
constant ccommand15 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B4",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand16 : OledMem :=((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"B1",X"B5",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                 (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));

constant ccommand0 : OledMem  := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),	
                                   (X"00",X"00",X"57",X"41",X"49",X"54",X"49",X"4E",X"47",X"00",X"00",X"46",X"4F",X"52",X"00",X"00"),
                                   (X"00",X"00",X"59",X"4F",X"55",X"52",X"00",X"00",X"41",X"4E",X"53",X"57",X"45",X"52",X"00",X"00"),
                                   (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));
                                   
constant ccommand99 : OledMem  := ((X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),	
                                  (X"00",X"00",X"00",X"47",X"41",X"4D",X"45",X"00",X"00",X"4F",X"56",X"45",X"52",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"),
                                  (X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00",X"00"));                                   

signal current_state : states := Idle;
signal after_state : states;
signal after_page_state : states;
signal after_char_state : states;
signal after_update_state : states;
signal temp_dc : std_logic := '0';
signal temp_delay_ms : std_logic_vector(11 downto 0);
signal temp_delay_en : std_logic := '0';
signal temp_delay_fin : std_logic;
signal temp_spi_en : std_logic := '0';
signal temp_spi_data : STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
signal temp_spi_fin : STD_LOGIC;
signal temp_char : STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
signal temp_addr : STD_LOGIC_VECTOR (10 downto 0) := (others => '0');
signal temp_dout : STD_LOGIC_VECTOR (7 downto 0);
signal temp_page : STD_LOGIC_VECTOR (1 downto 0) := (others => '0');
signal temp_index : integer range 0 to 15 := 0;
signal x_coord : integer := 0;
signal game_clk : std_logic := '0';
signal game_cnt : integer := 0;
begin
    DC<= temp_dc;
    FIN <= '1' when (current_state = Done) else '0';
    SPI_COMP: SPI_interface PORT MAP (
        CLK => CLK,
        RST => RST,
        SPI_EN => temp_spi_en,
        SPI_DATA => temp_spi_data,
        CS => CS,
        SDO => SDO,
        SCLK => SCLK,
        SPI_FIN => temp_spi_fin
    );
    DELAY_COMP: Delay PORT MAP (
        CLK => CLK,
        RST => RST,
        DELAY_MS => temp_delay_ms,
        DELAY_EN => temp_delay_en,
        DELAY_FIN => temp_delay_fin
    );
    CHAR_LIB_COMP : ascii_rom PORT MAP (
        clk => CLK,
        addr => temp_addr,
        dout => temp_dout
    );
    process(CLK, rst, gameOverv)
    begin
        if(rising_edge(CLK)) then
            if (rst = '1') then current_state <= Idle; end if;
            --if (gameOverv = '1') then current_state <= GameOver; end if;
            case(current_state) is 
                when Idle =>
                    if(EN = '1') then
                        current_state <= ClearDC;
                        case JUMP is
                            when "0000" => after_page_state <= Command1;
                            when "0001" => after_page_state <= Command2;
                            when "0010" => after_page_state <= Command3;
                            when "0011" => after_page_state <= Command4;
                            when "0100" => after_page_state <= Command5;
                            when "0101" => after_page_state <= Command6;
                            when "0110" => after_page_state <= Command7;
                            when "0111" => after_page_state <= Command8;
                            when "1000" => after_page_state <= Command9;
                            when "1001" => after_page_state <= Command10;
                            when "1010" => after_page_state <= Command11;
                            when "1011" => after_page_state <= Command12;
                            when "1100" => after_page_state <= Command13;
                            when "1101" => after_page_state <= Command14;
                            when "1110" => after_page_state <= Command15;
                            when "1111" => after_page_state <= Command16;                            
                        end case;
                        temp_page <= "00";
                    end if;
                -- Game Begins
                when Command1 =>
                    current_screen <= ccommand1;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Wait1 =>
                    temp_delay_ms <= "000111110100"; --500
                    
                    if (gameOverv = '1') then 
                        after_state <= GameOver; 
                    else 
                        if (show = '0') then 
                            after_state <= Command0; 
                        else 
                            case JUMP is
                                when "0000" => after_state <= Command1;
                                when "0001" => after_state <= Command2;
                                when "0010" => after_state <= Command3;
                                when "0011" => after_state <= Command4;
                                when "0100" => after_state <= Command5;
                                when "0101" => after_state <= Command6;
                                when "0110" => after_state <= Command7;
                                when "0111" => after_state <= Command8;
                                when "1000" => after_state <= Command9;
                                when "1001" => after_state <= Command10;
                                when "1010" => after_state <= Command11;
                                when "1011" => after_state <= Command12;
                                when "1100" => after_state <= Command13;
                                when "1101" => after_state <= Command14;
                                when "1110" => after_state <= Command15;
                                when "1111" => after_state <= Command16;                              
                            end case;
                        end if;
                    end if;
                    current_state<= Transition3;
                    
                when Command0 =>
                    current_screen<= ccommand0;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;    
                    
                when Command2 =>
                    current_screen<= ccommand2;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command3 =>
                    current_screen <= ccommand3;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command4 => 
                    current_screen <= ccommand4;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                
                when Command5 =>
                    current_screen <= ccommand5;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command6 =>
                    current_screen <= ccommand6;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                
                when Command7 =>
                    current_screen <= ccommand7;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command8 =>
                    current_screen <= ccommand8;
                    after_update_state <= Wait1;
                    current_state <= UpdateScreen;
                
                when Command9 =>
                    current_screen <= ccommand9;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command10 =>
                    current_screen <= ccommand10;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                
                when Command11 =>
                    current_screen <= ccommand11;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command12 =>
                    current_screen <= ccommand12;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
               
                when Command13 =>
                    current_screen <= ccommand13;
                    after_update_state <= Wait1;
                    current_state <= UpdateScreen;
                when Command14 =>
                    current_screen <= ccommand14;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                
                when Command15 =>
                    current_screen <= ccommand15;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                when Command16 =>
                    current_screen <= ccommand16;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                    
                when GameOver => 
                    current_screen <= ccommand99;
                    current_state <= UpdateScreen;
                    after_update_state <= Wait1;
                   
                    
              
               
                when Done =>
                    if(EN = '0') then
                        current_state <= Idle;
                    end if;
                when ClearDC =>
                    temp_dc<= '0';
                    current_state <= SetPage;
                when SetPage =>
                    temp_spi_data <= "00100010";
                    after_state <= PageNum;
					current_state <= Transition1;
				when PageNum =>
					temp_spi_data <= "000000" & temp_page;
					after_state <= LeftColumn1;
					current_state <= Transition1;
				when LeftColumn1 =>
					temp_spi_data <= "00000000";
					after_state <= LeftColumn2;
					current_state <= Transition1;
				when LeftColumn2 =>
					temp_spi_data <= "00010000";
					after_state <= SetDC;
					current_state <= Transition1;
				when SetDC =>
					temp_dc <= '1';
                    current_state <= after_page_state;


                when SendChar1 =>
					temp_addr <= temp_char & "000";
					after_state <= SendChar2;
					current_state <= ReadMem;
				when SendChar2 =>
					temp_addr <= temp_char & "001";
					after_state <= SendChar3;
					current_state <= ReadMem;
				when SendChar3 =>
					temp_addr <= temp_char & "010";
					after_state <= SendChar4;
					current_state <= ReadMem;
				when SendChar4 =>
					temp_addr <= temp_char & "011";
					after_state <= SendChar5;
					current_state <= ReadMem;
				when SendChar5 =>
					temp_addr <= temp_char & "100";
					after_state <= SendChar6;
					current_state <= ReadMem;
				when SendChar6 =>
					temp_addr <= temp_char & "101";
					after_state <= SendChar7;
					current_state <= ReadMem;
				when SendChar7 =>
					temp_addr <= temp_char & "110";
					after_state <= SendChar8;
					current_state <= ReadMem;
				when SendChar8 =>
					temp_addr <= temp_char & "111";
					after_state <= after_char_state;
					current_state <= ReadMem;
				when ReadMem =>
					current_state <= ReadMem2;
				when ReadMem2 =>
					temp_spi_data <= temp_dout;
					current_state <= Transition1;
                when Transition1 =>
					temp_spi_en <= '1';
					current_state <= Transition2;
				when Transition2 =>
					if(temp_spi_fin = '1') then
						current_state <= Transition5;
                    end if; 
                when Transition3 =>
					temp_delay_en <= '1';
					current_state <= Transition4;
				when Transition4 =>
					if(temp_delay_fin = '1') then
						current_state <= Transition5;
                    end if;
                when Transition5 =>
					temp_spi_en <= '0';
					temp_delay_en <= '0';
                    current_state <= after_state;

                when UpdateScreen => 
                    temp_char <= current_screen(CONV_INTEGER(temp_page),temp_index);
                    if(temp_index = 15) then	
                        temp_index <= 0;
                        temp_page <= temp_page + 1;
                        after_char_state <= ClearDC;
                        if(temp_page = "11") then
                            after_page_state <= after_update_state;
                        else	
                            after_page_state <= UpdateScreen;
                        end if;
                    else
                        temp_index <= temp_index + 1;
                        after_char_state <= UpdateScreen;
                    end if;
                    current_state <= SendChar1;
                when others =>
                    current_state <= Idle;
            end case;
        end if;
    end process;
   
end Behavioral;    
