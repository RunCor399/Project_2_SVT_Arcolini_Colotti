from colorama import Fore

class Logger: 
    '''
    Visual banner for the script.
    '''
    
    def __init__(self) -> None:
        self.header = '''
        ==============================================================================================================================
                                   _______    ________    ___   ____ ___  ___       ___  __ __  ____ _________
                                  / ____/ |  / / ____/   |__ \ / __ \__ \|__ \     |__ \/ // / ( __ <  / ____/
                                 / /    | | / / __/________/ // / / /_/ /__/ /_______/ / // /_/ __  / /___ \  
                                / /___  | |/ / /__/_____/ __// /_/ / __// __/_____/ __/__  __/ /_/ / /___/ /  
                                \____/  |___/_____/    /____/\____/____/____/    /____/ /_/  \____/_/_____/   
                                                                              
        ==============================================================================================================================
        JHipster is a development platform to quickly generate, develop, & deploy modern web applications & microservice architectures. 
        SQL Injection vulnerability in entities for applications generated with the option "reactive with Spring WebFlux" enabled and 
        an SQL database using r2dbc. Currently, SQL injection is possible in the findAllBy(Pageable pageable, Criteria criteria) 
        method of an entity repository class generated in these applications as the where clause using Criteria for queries are not 
        sanitized and user input is passed on as it is by the criteria. This issue has been patched in v7.8.1. 

        This script contains a verbose exploit script used as a Proof of Concept (PoC) for the CVE-2022-24815. More detailed information 
        can be found at: https://github.com/DavideArcolini/VulnerableMockApplication.
        
        Contact: davide.arcolini@studenti.polito.it
         
        '''
        
        
    def print_banner(self) -> None:
        print(self.header)
        
    def log(self, message: str) -> None:
        print(Fore.CYAN + '[+] ' + Fore.RESET + message)
        return
    
    def success(self, message: str) -> None:
        print(Fore.GREEN + '[!] ' + Fore.RESET + message)
        return
    
    def warning(self, message: str) -> None:
        print(Fore.RED + '[!] ' + Fore.RESET + message)
        return
    
    def request(self, verb: str, endpoint: str) -> None:
        print('    ' + Fore.CYAN + f'{verb} {endpoint}')
        return