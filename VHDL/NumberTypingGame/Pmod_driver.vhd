library IEEE; 
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.ALL;
use ieee.std_logic_arith.all;

entity Pmod_driver is
    Port(
        ADDR : in STD_LOGIC_VECTOR (3 downto 0);
        CLK : in  STD_LOGIC;
		RST : in  STD_LOGIC;
		gameOver : in  STD_LOGIC;
		show : in  STD_LOGIC;
		CS : out STD_LOGIC;
		SDIN : out STD_LOGIC;
		SCLK : out STD_LOGIC;
		DC : out STD_LOGIC;
		RES	: out STD_LOGIC;
		VBAT : out STD_LOGIC;
		VDD	: out STD_LOGIC);
end Pmod_driver;

architecture Behavioral of Pmod_driver is

COMPONENT OLEDInit is
    Port ( CLK 	: in  STD_LOGIC;
		RST 	: in	STD_LOGIC;
		EN		: in  STD_LOGIC;
		CS  	: out STD_LOGIC;
		SDO	: out STD_LOGIC;
		SCLK	: out STD_LOGIC;
		DC		: out STD_LOGIC;
		RES	: out STD_LOGIC;
		VBAT	: out STD_LOGIC;
		VDD	: out STD_LOGIC;
		FIN  : out STD_LOGIC);
end component;

--COMPONENT Disp is
--    Port (JUMP : in STD_LOGIC_VECTOR (3 downto 0); 
--        CLK 	: in  STD_LOGIC;
--		RST 	: in	STD_LOGIC;
--		EN		: in  STD_LOGIC;
--        gameOver : in  STD_LOGIC;
--		CS  	: out STD_LOGIC;
--		SDO		: out STD_LOGIC;
--		SCLK	: out STD_LOGIC;
--		DC		: out STD_LOGIC;
--        FIN  : out STD_LOGIC);
--end component;

type states is (Idle,
    OledInitialize,
    OledExample,
    Done);

signal current_state 	: states := Idle;

signal init_en				: STD_LOGIC := '0';
signal init_done			: STD_LOGIC;
signal init_cs				: STD_LOGIC;
signal init_sdo			: STD_LOGIC;
signal init_sclk			: STD_LOGIC;
signal init_dc				: STD_LOGIC;

signal example_en			: STD_LOGIC := '0';
signal example_cs			: STD_LOGIC;
signal example_sdo		: STD_LOGIC;
signal example_sclk		: STD_LOGIC;
signal example_dc			: STD_LOGIC;
signal example_done		: STD_LOGIC;

begin

Init: OLEDInit port map(CLK, RST, init_en, init_cs, init_sdo, init_sclk, init_dc, RES, VBAT, VDD, init_done);
Example: entity work.Disp Port map(ADDR, CLK, RST, example_en, gameOver, show, example_cs, example_sdo, example_sclk, example_dc, example_done);

--MUXes to indicate which outputs are routed out depending on which block is enabled
CS <= init_cs when (current_state = OledInitialize) else
example_cs;
SDIN <= init_sdo when (current_state = OledInitialize) else
example_sdo;
SCLK <= init_sclk when (current_state = OledInitialize) else
example_sclk;
DC <= init_dc when (current_state = OledInitialize) else
example_dc;
--END output MUXes

--MUXes that enable blocks when in the proper states
init_en <= '1' when (current_state = OledInitialize) else
    '0';
example_en <= '1' when (current_state = OledExample) else
    '0';
--END enable MUXes


process(CLK)
begin
if(rising_edge(CLK)) then
if(RST = '1') then
current_state <= Idle;
else
case(current_state) is
    when Idle =>
        current_state <= OledInitialize;
    --Go through the initialization sequence
    when OledInitialize =>
        if(init_done = '1') then
            current_state <= OledExample;
        end if;
    --Do example and Do nothing when finished
    when OledExample =>
        if(example_done = '1') then
            current_state <= Done;
        end if;
    --Do Nothing
    when Done =>
        current_state <= Done;
    when others =>
        current_state <= Idle;
end case;
end if;
end if;
end process;


end Behavioral;